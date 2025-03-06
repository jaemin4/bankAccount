package com.pro.feature.bankService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pro.feature.bankService.controller.request.BankAccountDepositParam;
import com.pro.feature.bankService.controller.request.BankAccountSaveParam;
import com.pro.feature.bankService.controller.request.BankAccountTransferParam;
import com.pro.feature.bankService.controller.request.BankAccountWithdrawParam;
import com.pro.feature.bankService.repository.mybatis.AccountRepository;
import com.pro.feature.bankService.repository.mybatis.BankAccountRepository;
import com.pro.feature.bankService.repository.mybatis.UserRepository;
import com.pro.feature.bankService.repository.mybatis.entity.AccountEntity;
import com.pro.feature.bankService.repository.mybatis.entity.BankAccountEntity;
import com.pro.feature.bankService.repository.mybatis.entity.UserEntity;
import com.pro.response.RestResult;
import com.pro.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankService {

    private final BankAccountRepository bankAccountRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public RestResult getAll() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("data", bankAccountRepository.getAll());

        return new RestResult("조회 성공", "true", data);
    }

    @Transactional
    public RestResult save(BankAccountSaveParam param) {
        if(param.getBalance() == null || param.getEmail() == null || param.getPassword() == null || param.getName() == null){
            return new RestResult("회원가입 오류","false");
        }

        String saveBankAccountId = ServiceUtil.createBankAccountId();

        userRepository.save(new UserEntity(
              param.getUser_id(),
              param.getName(),
              param.getRole(),
              param.getEmail(),
              bCryptPasswordEncoder.encode(param.getPassword())
        ));

        AccountEntity accountEntity = new AccountEntity(param.getBalance());
        accountRepository.save(accountEntity);

        bankAccountRepository.save(new BankAccountEntity(
                    saveBankAccountId,
                    accountEntity.getAccount_number(),
                    param.getUser_id()));

        return new RestResult("가입 성공", "true");
    }

    @Transactional
    public RestResult deposit(BankAccountDepositParam param) {
        if(param.getBalance() == null || param.getAccount_number() == null){
            return new RestResult("입금 실패 : 계좌 번호가 없습니다.", "false");
        }
        BalanceLogParam balanceLogParam = new BalanceLogParam();

        AccountEntity prevAccount = accountRepository.findById(new AccountEntity(
                param.getAccount_number(),param.getBalance()
        ));
        if (prevAccount == null) {
            throw new IllegalStateException("입금 실패: 기존 계좌 정보를 찾을 수 없습니다.");
        }


        String fullMethodName = this.getClass().getSimpleName() + "." +
                new Object() {}.getClass().getEnclosingMethod().getName();
        balanceLogParam.setClassMethod(Map.of("currentMethod",fullMethodName));

        balanceLogParam.setPrevData(new LinkedHashMap<>(Map.of(
                "account_number",prevAccount.getAccount_number(),
                "balance", prevAccount.getBalance()
        )));
        prevAccount.setBalance(prevAccount.getBalance() + param.getBalance());
        accountRepository.updateAccountBalance(prevAccount);

        AccountEntity updatedAccount = accountRepository.findById(new AccountEntity(param.getAccount_number()));
        if(updatedAccount == null){
            throw new IllegalStateException("알 수 없는 오류: 업데이트된 계좌 정보를 찾을 수 없습니다.");
        }

        balanceLogParam.setCurrentData(new LinkedHashMap<>(Map.of(
                "account_number", updatedAccount.getAccount_number(),
                "balance", updatedAccount.getBalance()
        )));

        try {
            log.info("              BalanceLog : {}", objectMapper.writeValueAsString(balanceLogParam));
        }catch (JsonProcessingException e){
            log.info("     [{}] : {}",fullMethodName,e.getMessage());
        }
        rabbitTemplate.convertAndSend("bank.exchange","bank.log.deposit", balanceLogParam);

        return new RestResult("입금 성공", "true");
    }

    @Transactional
    public RestResult withdraw(BankAccountWithdrawParam param) {

        if(param.getBalance() == null || param.getAccount_number() == null){
            return new RestResult("출금 실패 : 계좌 번호가 없습니다.", "false");
        }
        BalanceLogParam balanceLogParam = new BalanceLogParam();

        AccountEntity prevAccount = accountRepository.findById(new AccountEntity(
                param.getAccount_number(),param.getBalance()
        ));
        if (prevAccount == null) {
            throw new IllegalStateException("출금 실패: 기존 계좌 정보를 찾을 수 없습니다.");
        }

        if (prevAccount.getBalance() < param.getBalance()) {
            return new RestResult("출금 실패: 잔액 부족", "false");
        }

        String fullMethodName = this.getClass().getSimpleName() + "." +
                new Object() {}.getClass().getEnclosingMethod().getName();
        balanceLogParam.setClassMethod(Map.of("currentMethod",fullMethodName));

        balanceLogParam.setPrevData(new LinkedHashMap<>(Map.of(
                "account_number",prevAccount.getAccount_number(),
                "balance", prevAccount.getBalance()
        )));

        prevAccount.setBalance(prevAccount.getBalance() - param.getBalance());
        accountRepository.updateAccountBalance(prevAccount);

        AccountEntity updatedAccount = accountRepository.findById(new AccountEntity(param.getAccount_number()));
        if(updatedAccount == null){
            throw new IllegalStateException("알 수 없는 오류: 업데이트된 계좌 정보를 찾을 수 없습니다.");
        }

        balanceLogParam.setCurrentData(new LinkedHashMap<>(Map.of(
                "account_number", updatedAccount.getAccount_number(),
                "balance", updatedAccount.getBalance()
        )));

        try {
            log.info("     BalanceLog : {}", objectMapper.writeValueAsString(balanceLogParam));
        }catch (JsonProcessingException e){
            log.info("     [{}] : {}",fullMethodName,e.getMessage());
        }
        rabbitTemplate.convertAndSend("bank.exchange","bank.log.withdraw", balanceLogParam);

        return new RestResult("출금 성공", "true");

    }

    @Transactional
    public RestResult transfer(BankAccountTransferParam param) {
        if (param.getBalance() == null || param.getFromAccountNumber() == null || param.getTo_account_number() == null) {
            return new RestResult("이체 실패 : 계좌 번호가 없습니다.", "false");
        }
        if (param.getBalance() <= 0) {
            return new RestResult("이체 실패 : 이체 금액을 확인해주세요.", "false");
        }

        BalanceLogParam balanceLogParam = new BalanceLogParam();

        AccountEntity fromAccountResult = accountRepository.findById(
                new AccountEntity(
                        param.getFromAccountNumber(),
                        param.getBalance()));

        AccountEntity toAccountResult = accountRepository.findById(
                new AccountEntity(
                        param.getTo_account_number(),
                        param.getBalance()));

        if (fromAccountResult == null || toAccountResult == null) {
            return new RestResult("이체 실패: 계좌를 찾을 수 없습니다.", "false");
        }

        if (toAccountResult.getBalance() < param.getBalance()) {
            return new RestResult("이체 실패: 잔액 부족", "false");
        }

        String fullMethodName = this.getClass().getSimpleName() + "." +
                new Object() {}.getClass().getEnclosingMethod().getName();
        balanceLogParam.setClassMethod(Map.of("currentMethod",fullMethodName));


        balanceLogParam.setPrevData(new LinkedHashMap<>(Map.of(
                "from_account_number",fromAccountResult.getAccount_number(),
                "from_balance",fromAccountResult.getBalance(),
                "to_account_number",toAccountResult.getAccount_number(),
                "to_balance", toAccountResult.getBalance()
        )));

        fromAccountResult.setBalance(fromAccountResult.getBalance() - param.getBalance());
        toAccountResult.setBalance(toAccountResult.getBalance() + param.getBalance());

        accountRepository.updateAccountBalance(fromAccountResult);
        accountRepository.updateAccountBalance(toAccountResult);

        AccountEntity uptFromAccountResult = accountRepository.findById(fromAccountResult);
        AccountEntity uptToAccountResult = accountRepository.findById(toAccountResult);

        if(uptFromAccountResult == null || uptToAccountResult == null){
            throw new IllegalStateException("알 수 없는 오류: 업데이트된 계좌 정보를 찾을 수 없습니다.");
        }

        balanceLogParam.setCurrentData(new LinkedHashMap<>(Map.of(
                "from_account_number",uptFromAccountResult.getAccount_number(),
                "from_balance",uptFromAccountResult.getBalance(),
                "to_account_number",uptToAccountResult.getAccount_number(),
                "to_balance", uptToAccountResult.getBalance()
        )));

        try {
            log.info("     BalanceLog : {}", objectMapper.writeValueAsString(balanceLogParam));
        }catch (JsonProcessingException e){
            log.info("     [{}] : {}",fullMethodName,e.getMessage());
        }
        rabbitTemplate.convertAndSend("bank.exchange","bank.log.transfer", balanceLogParam);
        return new RestResult("이체 성공", "true");
    }
}

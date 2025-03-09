package com.pro.service.persist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pro.exception.BankRuntimeException;
import com.pro.model.param.*;
import com.pro.repository.AccountRepository;
import com.pro.repository.BankAccountRepository;
import com.pro.repository.UserRepository;
import com.pro.model.entity.AccountEntity;
import com.pro.model.entity.BankAccountEntity;
import com.pro.model.entity.UserEntity;
import com.pro.model.result.RestResult;
import com.pro.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.pro.util.Utils.toJson;
import static com.pro.util.ValidationChecker.transferValidationCheck;
import static com.pro.util.ValidationChecker.withrdrawValidationCheck;

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
    public void deposit(BankAccountDepositParam param, AccountEntity savedAccount) {

        savedAccount.setBalance(savedAccount.getBalance() + param.getBalance());
        accountRepository.updateAccountBalance(savedAccount);

        log.info("입금 성공 : {}", toJson(savedAccount));
    }

    @Transactional
    public RestResult withdraw(BankAccountWithdrawParam param) {

        AccountEntity prevAccount = accountRepository.findById(new AccountEntity(
                param.getAccount_number(),param.getBalance()
        ));

        withrdrawValidationCheck(param, prevAccount);

        BalanceLogParam balanceLogParam = new BalanceLogParam();
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

        log.info("     BalanceLog : {}", toJson(balanceLogParam));
        rabbitTemplate.convertAndSend("bank.exchange","bank.log.withdraw", balanceLogParam);

        return new RestResult("출금 성공", "true");

    }


    @Transactional
    public RestResult transfer(BankAccountTransferParam param) {

        transferValidationCheck(param);

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


    public AccountEntity getBankAccount(Long accountNumber) {
        final AccountEntity accountEntity = accountRepository.findById(accountNumber);
        if(accountEntity == null){
            throw new BankRuntimeException("입금 실패: 기존 계좌 정보를 찾을 수 없습니다.");
        }
        return accountEntity;
    }
}

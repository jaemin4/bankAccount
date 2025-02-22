package com.pro.bankService.service;

import com.pro.bankService.controller.request.BankAccountDepositParam;
import com.pro.bankService.controller.request.BankAccountSaveParam;
import com.pro.bankService.controller.request.BankAccountTransferParam;
import com.pro.bankService.controller.request.BankAccountWithdrawParam;
import com.pro.bankService.repository.mybatis.AccountRepository;
import com.pro.bankService.repository.mybatis.BankAccountRepository;
import com.pro.bankService.repository.mybatis.UserRepository;
import com.pro.bankService.repository.mybatis.entity.AccountEntity;
import com.pro.bankService.repository.mybatis.entity.BankAccountEntity;
import com.pro.bankService.repository.mybatis.entity.UserEntity;
import com.pro.bankService.service.response.RestResult;
import com.pro.bankService.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;


// todo 실제로 Controller 통해서 테스트가 될수 있도록 연결해서 한번 해보세요.
// todo 아래 각 메소드의 로그상에서 balance 가 변하는 것을 확인할 수 있도록 로그를 추가해주세요. 로그만 보더라도 어떤 유저의 밸런스가 어떻게 변해가는지를 확인할 수 있으면 좋겠습니다.
//  예) success deposit balance: 1000 -> 2000
//  예) success withdrawal balance: 2000 -> 1000
//  기타 다른곳에 필요해 보이는 곳에도 추가로 해보면 좋겠습니다.
// todo 밸런스 히스토리를 남길수 있도록 BalanceLog 테이블을 만들어 주세요. 즉 거래내역을 남기는 일입니다.
@Slf4j
@Service
@RequiredArgsConstructor
public class BankService {

    private final BankAccountRepository bankAccountRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public RestResult getAll() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("data", bankAccountRepository.getAll());

        log.info("BankJpaService/getAll/success");
        return new RestResult("조회 성공", "true", data);
    }

    @Transactional
    public RestResult save(BankAccountSaveParam param) {
        String saveUserId = ServiceUtil.createUserId();
        String saveBankAccountId = ServiceUtil.createBankAccountId();

        userRepository.save(
                new UserEntity(
                    saveUserId,
                    param.getName()));

        AccountEntity accountEntity =
                new AccountEntity(
                    param.getBalance());

        accountRepository.save(accountEntity);

        bankAccountRepository.save(
                new BankAccountEntity(
                    saveBankAccountId,
                    accountEntity.getAccount_number(),
                    saveUserId));

        log.info("BankJpaService/save/success");
        return new RestResult("가입 성공", "true");
    }

    @Transactional
    public RestResult deposit(BankAccountDepositParam param) {
        AccountEntity accountEntity =
                new AccountEntity(
                    param.getAccount_number(),
                    param.getBalance());

        AccountEntity resultAccountFindById =
                accountRepository.findById(accountEntity);

        log.info("입금 계좌 : " + resultAccountFindById.getAccount_number());

        if (resultAccountFindById == null) {
            return new RestResult(
                    "입금 실패: 계좌를 찾을 수 없습니다.",
                    "false");
        }

        resultAccountFindById.setBalance(resultAccountFindById.getBalance() + param.getBalance());

        accountRepository.updateAccountBalance(resultAccountFindById);

        log.info("BankJpaService/deposit/success");
        return new RestResult("입금 성공", "true");
    }

    @Transactional
    public RestResult withdraw(BankAccountWithdrawParam param) {
        AccountEntity accountEntity =  accountRepository.findById(
                new AccountEntity(
                        param.getAccount_number(),
                        param.getBalance()));

        if (accountEntity == null) {
            return new RestResult(
                    "출금 실패: 계좌를 찾을 수 없습니다.",
                    "false");
        }

        if (accountEntity.getBalance() < param.getBalance()) {
            return new RestResult(
                    "출금 실패: 잔액 부족",
                    "false");
        }

        accountEntity.setBalance(accountEntity.getBalance() - param.getBalance());
        accountRepository.updateAccountBalance(accountEntity);

        log.info("BankJpaService/withdraw/success");
        return new RestResult("출금 성공", "true");
    }

    @Transactional
    public RestResult transfer(BankAccountTransferParam param) {
        AccountEntity fromAccountOpt = accountRepository.findById(
                new AccountEntity(
                        param.getFromAccountNumber(),
                        param.getBalance()));

        AccountEntity toAccountOpt = accountRepository.findById(
                new AccountEntity(
                        param.getToAccountNumber(),
                        param.getBalance()));

        if (fromAccountOpt == null || toAccountOpt == null) {
            return new RestResult("이체 실패: 계좌를 찾을 수 없습니다.", "false");
        }

        if (toAccountOpt.getBalance() < param.getBalance()) {
            return new RestResult("이체 실패: 잔액 부족", "false");
        }

        fromAccountOpt.setBalance(fromAccountOpt.getBalance() - param.getBalance());
        toAccountOpt.setBalance(toAccountOpt.getBalance() + param.getBalance());

        accountRepository.updateAccountBalance(fromAccountOpt);
        accountRepository.updateAccountBalance(toAccountOpt);

        log.info("BankJpaService/transfer/success");
        return new RestResult("이체 성공", "true");
    }
}

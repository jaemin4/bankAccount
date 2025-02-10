package com.account.pro.bankService.service;

import com.account.pro.bankService.controller.req.BankAccountDepositParam;
import com.account.pro.bankService.controller.req.BankAccountSaveParam;
import com.account.pro.bankService.controller.req.BankAccountTransferParam;
import com.account.pro.bankService.controller.req.BankAccountWithdrawParam;
import com.account.pro.bankService.controller.res.RestResult;
import com.account.pro.bankService.repository.BankAccountRepository;
import com.account.pro.bankService.repository.entity.Account;
import com.account.pro.bankService.repository.entity.BankAccount;
import com.account.pro.bankService.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankFrontService {

    private final BankAccountRepository bankAccountRepository;

    public RestResult getAll() {
        Map<String, Object> data = new LinkedHashMap<>(Map.of(
                "data",bankAccountRepository.getAll()
        ));
        log.info("BankFrontService/getAll/success");

        return new RestResult("조회 성공","true", data);
    }

    // todo : tx 후 로그 필수자리 -> bankAccountRepository.save(bankAccountEntity); 바로 직후
    public RestResult save(BankAccountSaveParam param) {
        BankAccount bankAccountEntity = new BankAccount(
                new Account(param.getAccountNumber(),param.getBalance()),
                new User(param.getId(), param.getName())
        );

        bankAccountRepository.save(bankAccountEntity);
        log.info("BankFrontService/save/success");

        return new RestResult("가입 성공","true");
    }

    public RestResult deposit(BankAccountDepositParam param){
        Account accountEntity = new Account(param.getAccountNumber(),param.getBalance());
        bankAccountRepository.deposit(accountEntity);
        log.info("BankFrontService/deposit/success");

        return new RestResult("입금 성공","true");
    }

    public RestResult withdraw(BankAccountWithdrawParam param){
        Account accountEntity = new Account(param.getAccountNumber(),param.getBalance());
        bankAccountRepository.withdraw(accountEntity);
        log.info("BankFrontService/withdraw/success");

        return new RestResult("출금 성공","true");
    }

    public RestResult transfer(BankAccountTransferParam param) {

        Account depositAccount = new Account(param.getToAccountNumber(), param.getBalance());
        bankAccountRepository.deposit(depositAccount);
        log.info("BankFrontService/transfer-deposit/success");

        Account withdrawAccount = new Account(param.getFromAccountNumber(), param.getBalance());
        bankAccountRepository.withdraw(withdrawAccount);
        log.info("BankFrontService/transfer-withdraw/success");

        return new RestResult("이체 성공","true");

    }

}



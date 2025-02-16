package com.account.pro.bankService.service;

import com.account.pro.bankService.controller.request.BankAccountDepositParam;
import com.account.pro.bankService.controller.request.BankAccountSaveParam;
import com.account.pro.bankService.controller.request.BankAccountTransferParam;
import com.account.pro.bankService.controller.request.BankAccountWithdrawParam;
import com.account.pro.bankService.service.response.RestResult;
import com.account.pro.bankService.repository.BankAccountRepository;
import com.account.pro.bankService.repository.entity.Account;
import com.account.pro.bankService.repository.entity.BankAccount;
import com.account.pro.bankService.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;

// Aggregate Pattern
@Slf4j
@Service
@RequiredArgsConstructor
public class BankFrontService {

    // todo: 레이어를 하나 더 추가해주세요. Controller --> FrontService --> Service --> Repository
    // todo: Repository 를 Memory 가 아니라 DB로 저장하도록 코드를 변경해주세요.
    // todo: 제가 AWS 에 설치하고 운용하고 있는 MySQL 계정을 드립니다. 여기에 앞으로 작업해주시면 됩니다.
    //  -> url: mbslaw-restored.cszatmtp17tv.ap-northeast-2.rds.amazonaws.com
    //  -> db: min_bank
    //  -> username: min_bank
    //  -> password: @@min_bank##!!
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



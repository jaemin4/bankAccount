package com.account.pro.bankService.service;

import com.account.pro.bankService.controller.request.BankAccountDepositParam;
import com.account.pro.bankService.controller.request.BankAccountSaveParam;
import com.account.pro.bankService.controller.request.BankAccountTransferParam;
import com.account.pro.bankService.controller.request.BankAccountWithdrawParam;
import com.account.pro.bankService.repository.entity.Account;
import com.account.pro.bankService.repository.entity.BankAccount;
import com.account.pro.bankService.repository.entity.User;
import com.account.pro.bankService.service.response.RestResult;
import com.account.pro.bankService.repository.AccountRepository;
import com.account.pro.bankService.repository.BankAccountdRepository;
import com.account.pro.bankService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankServiceDatabase implements BankInterfaceService {

    private final BankAccountdRepository bankAccountdRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    public RestResult getAll() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("data", bankAccountdRepository.findAll());

        log.info("BankJpaService/getAll/success");
        return new RestResult("조회 성공", "true", data);
    }

    @Transactional
    @Override
    public RestResult save(BankAccountSaveParam param) {
        User user = new User(param.getId(), param.getName());
        Account account = new Account(param.getAccountNumber(), param.getBalance());
        userRepository.save(user);
        accountRepository.save(account);

        BankAccount bankAccount = new BankAccount(account, user);
        bankAccountdRepository.save(bankAccount);

        log.info("BankJpaService/save/success");
        return new RestResult("가입 성공", "true");
    }

    @Transactional
    @Override
    public RestResult deposit(BankAccountDepositParam param) {
        Optional<Account> optionalAccount = accountRepository.findById(param.getAccountNumber());

        if (optionalAccount.isEmpty()) {
            return new RestResult("입금 실패: 계좌를 찾을 수 없습니다.", "false");
        }

        Account account = optionalAccount.get();
        account.setBalance(account.getBalance() + param.getBalance());
        accountRepository.save(account);

        log.info("BankJpaService/deposit/success");
        return new RestResult("입금 성공", "true");
    }

    @Transactional
    @Override
    public RestResult withdraw(BankAccountWithdrawParam param) {
        Optional<Account> optionalAccount = accountRepository.findById(param.getAccountNumber());

        if (optionalAccount.isEmpty()) {
            return new RestResult("출금 실패: 계좌를 찾을 수 없습니다.", "false");
        }

        Account account = optionalAccount.get();
        if (account.getBalance() < param.getBalance()) {
            return new RestResult("출금 실패: 잔액 부족", "false");
        }

        account.setBalance(account.getBalance() - param.getBalance());
        accountRepository.save(account);

        log.info("BankJpaService/withdraw/success");
        return new RestResult("출금 성공", "true");
    }

    @Transactional
    @Override
    public RestResult transfer(BankAccountTransferParam param) {
        Optional<Account> fromAccountOpt = accountRepository.findById(param.getFromAccountNumber());
        Optional<Account> toAccountOpt = accountRepository.findById(param.getToAccountNumber());

        if (fromAccountOpt.isEmpty() || toAccountOpt.isEmpty()) {
            return new RestResult("이체 실패: 계좌를 찾을 수 없습니다.", "false");
        }

        Account fromAccount = fromAccountOpt.get();
        Account toAccount = toAccountOpt.get();

        if (fromAccount.getBalance() < param.getBalance()) {
            return new RestResult("이체 실패: 잔액 부족", "false");
        }

        fromAccount.setBalance(fromAccount.getBalance() - param.getBalance());
        toAccount.setBalance(toAccount.getBalance() + param.getBalance());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        log.info("BankJpaService/transfer/success");
        return new RestResult("이체 성공", "true");
    }
}

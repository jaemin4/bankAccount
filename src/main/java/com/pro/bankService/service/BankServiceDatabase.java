package com.pro.bankService.service;

import com.pro.bankService.controller.request.BankAccountDepositParam;
import com.pro.bankService.controller.request.BankAccountSaveParam;
import com.pro.bankService.controller.request.BankAccountTransferParam;
import com.pro.bankService.controller.request.BankAccountWithdrawParam;
import com.pro.bankService.repository.entity.Account;
import com.pro.bankService.repository.entity.BankAccount;
import com.pro.bankService.repository.entity.User;
import com.pro.bankService.service.response.RestResult;
import com.pro.bankService.repository.AccountRepository;
import com.pro.bankService.repository.BankAccountdRepository;
import com.pro.bankService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

// todo 실제로 Controller 통해서 테스트가 될수 있도록 연결해서 한번 해보세요.
// todo 아래 각 메소드의 로그상에서 balance 가 변하는 것을 확인할 수 있도록 로그를 추가해주세요. 로그만 보더라도 어떤 유저의 밸런스가 어떻게 변해가는지를 확인할 수 있으면 좋겠습니다.
//  예) success deposit balance: 1000 -> 2000
//  예) success withdrawal balance: 2000 -> 1000
//  기타 다른곳에 필요해 보이는 곳에도 추가로 해보면 좋겠습니다.
// todo 밸런스 히스토리를 남길수 있도록 BalanceLog 테이블을 만들어 주세요. 즉 거래내역을 남기는 일입니다.
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

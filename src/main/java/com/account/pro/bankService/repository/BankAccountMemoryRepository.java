package com.account.pro.bankService.repository;

import com.account.pro.bankService.repository.entity.Account;
import com.account.pro.bankService.repository.entity.BankAccount;
import com.account.pro.bankService.memory.BankAccountMemory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


import java.util.*;

@Repository
@Slf4j
public class BankAccountMemoryRepository implements BankAccountRepository{

    private final Map<Long, BankAccount> accountMap = BankAccountMemory.getAccountMap();

    @Override
    public Collection<BankAccount> getAll() {
        return new ArrayList<>(accountMap.values());
    }

    @Override
    public void save(BankAccount bankAccount) {
        accountMap.put(accountMap.isEmpty() ? 1L : Collections.max(accountMap.keySet()) + 1,
                       bankAccount);
    }

    @Override
    public void deposit(Account account) {
        BankAccount bankAccount = accountMap.values().stream()
                .filter(b -> b.getAccount().getAccountNumber() == account.getAccountNumber())
                .findFirst()
                .orElse(null);

        if (bankAccount != null) {
            Account existingAccount = bankAccount.getAccount();
            existingAccount.setBalance(existingAccount.getBalance() + account.getBalance());
        } else {
            throw new IllegalArgumentException("해당 계좌가 존재하지 않습니다.");
        }
    }

    @Override
    public void withdraw(Account account) {
        BankAccount bankAccount = accountMap.values().stream()
                .filter(b -> b.getAccount().getAccountNumber() == account.getAccountNumber())
                .findFirst()
                .orElse(null);

        if(bankAccount != null){
            Account exsitingAccount = bankAccount.getAccount();
            exsitingAccount.setBalance(exsitingAccount.getBalance() - account.getBalance());
        } else{
            throw new IllegalArgumentException("해당 계좌가 존재하지 않습니다.");
        }

    }
}

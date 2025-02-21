package com.pro.bankService.repository;

import com.pro.bankService.repository.entity.Account;
import com.pro.bankService.repository.entity.BankAccount;

import java.util.Collection;

public interface BankAccountRepository {

    // todo : public ?
    Collection<BankAccount> getAll();
    void save(BankAccount bankAccount);
    void deposit(Account account);
    void withdraw(Account account);

}

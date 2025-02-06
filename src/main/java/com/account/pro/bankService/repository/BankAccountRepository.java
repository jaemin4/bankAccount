package com.account.pro.bankService.repository;

import com.account.pro.bankService.repository.entity.Account;
import com.account.pro.bankService.repository.entity.BankAccount;

import java.util.Collection;

public interface BankAccountRepository {

    public Collection<BankAccount> getAll();
    public void save(BankAccount bankAccount);
    public void deposit(Account account);
    public void withdraw(Account account);

}

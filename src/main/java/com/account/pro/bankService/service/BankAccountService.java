package com.account.pro.bankService.service;


import com.account.pro.bankService.repository.entity.Account;
import com.account.pro.bankService.repository.entity.BankAccount;
import com.account.pro.bankService.service.req.SerReqDepositeAccountDto;
import com.account.pro.bankService.service.req.SerReqSaveBankAccountDto;
import com.account.pro.bankService.service.req.SerReqTransformAccountDto;
import com.account.pro.bankService.service.req.SerReqWithdrawAccountDto;

import java.util.Collection;

public interface BankAccountService {

    public void save(SerReqSaveBankAccountDto serReqSaveBankAccountDto);
    public Collection<BankAccount> getAll();
    public void deposit(SerReqDepositeAccountDto serReqDepositeAccountDto);
    public void withdraw(SerReqWithdrawAccountDto serReqWithdrawAccountDto);
    public void transform(SerReqTransformAccountDto serReqTransformAccountDto);

}

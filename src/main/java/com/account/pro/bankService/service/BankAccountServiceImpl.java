package com.account.pro.bankService.service;

import com.account.pro.bankService.repository.entity.Account;
import com.account.pro.bankService.repository.entity.BankAccount;
import com.account.pro.bankService.repository.BankAccountMemoryRepository;

import com.account.pro.bankService.service.req.SerReqDepositeAccountDto;
import com.account.pro.bankService.service.req.SerReqSaveBankAccountDto;
import com.account.pro.bankService.service.req.SerReqTransformAccountDto;
import com.account.pro.bankService.service.req.SerReqWithdrawAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

// todo: persistence logic layer
@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService{

    private final BankAccountMemoryRepository bankAccountMemoryRepository;

    @Override
    public Collection<BankAccount> getAll() {
        return bankAccountMemoryRepository.getAll();
    }

    @Override
    public void save(SerReqSaveBankAccountDto serReqSaveBankAccountDto) {
        //bankAccount 검증
        //추가사항 처리

        BankAccount bankAccount = serReqSaveBankAccountDto.toBankAccount();
        bankAccountMemoryRepository.save(bankAccount);
    }

    @Override
    public void deposit(SerReqDepositeAccountDto serReqDepositeAccountDto) {
        Account account = serReqDepositeAccountDto.toAccount();
        bankAccountMemoryRepository.deposit(account);

    }

    @Override
    public void withdraw(SerReqWithdrawAccountDto serReqWithdrawAccountDto) {
        Account account = serReqWithdrawAccountDto.toAccount();
        bankAccountMemoryRepository.withdraw(account);
    }

    @Override
    public void transform(SerReqTransformAccountDto serReqTransformAccountDto) {

        Account depositAccount = serReqTransformAccountDto.toAccount();
        bankAccountMemoryRepository.deposit(depositAccount);

        Account withdrawAccount = serReqTransformAccountDto.fromAccount();
        bankAccountMemoryRepository.withdraw(withdrawAccount);

    }
}


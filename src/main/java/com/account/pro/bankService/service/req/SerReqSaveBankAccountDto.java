package com.account.pro.bankService.service.req;

import com.account.pro.bankService.repository.entity.Account;
import com.account.pro.bankService.repository.entity.BankAccount;
import com.account.pro.bankService.repository.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Builder
public class SerReqSaveBankAccountDto {
    private Long accountNumber;
    private int balance;
    private String name;
    private Long id;

    public BankAccount toBankAccount() {
        return BankAccount.builder()
                .account(Account.builder()
                        .accountNumber(accountNumber)
                        .balance(balance)
                        .build())
                .user(User.builder()
                        .id(id)
                        .name(name)
                        .build())
                .build();
    }

}

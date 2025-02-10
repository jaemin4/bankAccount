package com.account.pro.bankService.repository.entity;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
public class BankAccount {
    private Account account;
    private User user;

}

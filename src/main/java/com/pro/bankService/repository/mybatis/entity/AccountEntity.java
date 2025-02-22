package com.pro.bankService.repository.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {

    private Long account_number;
    private double balance;

    public AccountEntity(double balance) {
        this.balance = balance;
    }
    public AccountEntity(Long account_number) {
        this.account_number = account_number;
    }

}

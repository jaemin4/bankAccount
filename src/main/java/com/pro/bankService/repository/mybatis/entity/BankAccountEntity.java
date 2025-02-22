package com.pro.bankService.repository.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountEntity {

    private String bank_account_id;
    private Long account_number;
    private String user_id;

    public BankAccountEntity(Long account_number, String user_id) {
        this.account_number = account_number;
        this.user_id = user_id;
    }
}

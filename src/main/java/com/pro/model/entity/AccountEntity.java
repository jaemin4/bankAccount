package com.pro.model.entity;

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
    private Long balance;


    public AccountEntity(Long account_number) {
        this.account_number = account_number;
    }

}

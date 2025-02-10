package com.account.pro.bankService.controller.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountSaveParam {
    private Long accountNumber;
    private int balance;
    private String name;
    private Long id;
}

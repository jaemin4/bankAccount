package com.account.pro.bankService.controller.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountTransferParam {
    private Long toAccountNumber;
    private Long fromAccountNumber;
    private int balance;

}

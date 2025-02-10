package com.account.pro.bankService.controller.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountWithdrawParam {
    private Long accountNumber;
    private int balance;

}

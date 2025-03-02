package com.pro.bankService.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountDepositParam {
    private Long account_number;
    private Long balance;

}

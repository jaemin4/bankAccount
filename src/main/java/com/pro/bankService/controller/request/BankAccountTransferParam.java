package com.pro.bankService.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountTransferParam {
    private Long toAccountNumber;
    private Long fromAccountNumber;
    private Long balance;

}

package com.pro.feature.bankService.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountTransferParam {
    private Long to_account_number;
    private Long fromAccountNumber;
    private Long balance;

}

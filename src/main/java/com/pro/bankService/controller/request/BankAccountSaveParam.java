package com.pro.bankService.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountSaveParam {
    private Long balance;
    private String name;
}

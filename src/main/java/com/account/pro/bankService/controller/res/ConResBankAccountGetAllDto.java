package com.account.pro.bankService.controller.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConResBankAccountGetAllDto {
    private Long accountNumber;
    private int balance;
    private String name;
    private Long id;
}

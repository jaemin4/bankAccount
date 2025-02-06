package com.account.pro.bankService.controller.req;


import com.account.pro.bankService.service.req.SerReqSaveBankAccountDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountParam {
    private Long accountNumber;
    private int balance;
    private String name;
    private Long id;
}

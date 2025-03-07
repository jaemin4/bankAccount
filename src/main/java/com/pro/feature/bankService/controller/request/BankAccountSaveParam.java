package com.pro.feature.bankService.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountSaveParam {
    private Long balance;
    private String name;
    private String email;
    private String password;
    private String role;
    private String user_id;
}

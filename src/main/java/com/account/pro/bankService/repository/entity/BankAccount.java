package com.account.pro.bankService.repository.entity;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    private Account account;
    private User user;

}

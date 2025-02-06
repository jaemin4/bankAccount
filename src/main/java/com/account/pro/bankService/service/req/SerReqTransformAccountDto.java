package com.account.pro.bankService.service.req;

import com.account.pro.bankService.repository.entity.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SerReqTransformAccountDto {

    private Long toAccountNumber;
    private Long fromAccountNumber;
    private int balance;

    public Account toAccount(){
        return Account.builder()
                .accountNumber(this.toAccountNumber)
                .balance(this.balance)
                .build();
    }

    public Account fromAccount(){
        return Account.builder()
                .accountNumber(this.fromAccountNumber)
                .balance(this.balance)
                .build();
    }

}

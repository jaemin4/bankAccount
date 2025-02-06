package com.account.pro.bankService.service.req;

import com.account.pro.bankService.repository.entity.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter

public class SerReqDepositeAccountDto {
    private Long accountNumber;
    private int balance;

    public Account toAccount(){
        return Account.builder()
                .accountNumber(this.accountNumber)
                .balance(this.balance)
                .build();
    }

}

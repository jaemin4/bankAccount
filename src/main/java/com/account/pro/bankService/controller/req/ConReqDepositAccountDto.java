package com.account.pro.bankService.controller.req;

import com.account.pro.bankService.service.req.SerReqDepositeAccountDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConReqDepositAccountDto {
    private Long accountNumber;
    private int balance;

    public SerReqDepositeAccountDto toSerReqDepositeAccountDto(){
        return SerReqDepositeAccountDto.builder()
                .accountNumber(this.accountNumber)
                .balance(this.balance)
                .build();
    }

}

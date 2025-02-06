package com.account.pro.bankService.controller.req;

import com.account.pro.bankService.service.req.SerReqWithdrawAccountDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConReqWithdrawAccountDto {
    private Long accountNumber;
    private int balance;

    public SerReqWithdrawAccountDto toSerReqWithdrawAccountDto(){
        return SerReqWithdrawAccountDto.builder()
                .accountNumber(this.accountNumber)
                .balance(this.balance)
                .build();
    }
}

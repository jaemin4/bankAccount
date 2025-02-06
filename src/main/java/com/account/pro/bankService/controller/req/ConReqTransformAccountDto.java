package com.account.pro.bankService.controller.req;

import com.account.pro.bankService.service.req.SerReqTransformAccountDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConReqTransformAccountDto {
    private Long toAccountNumber;
    private Long fromAccountNumber;
    private int balance;

    public SerReqTransformAccountDto toSerReqTransformAccountDto(){
        return SerReqTransformAccountDto.builder()
                .toAccountNumber(this.toAccountNumber)
                .fromAccountNumber(this.fromAccountNumber)
                .balance(this.balance)
                .build();
    }


}

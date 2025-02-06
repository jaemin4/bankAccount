package com.account.pro.bankService.controller.req;


import com.account.pro.bankService.service.req.SerReqSaveBankAccountDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConReqSaveBankAccountDto {

    private Long accountNumber;
    private int balance;
    private String name;
    private Long id;

    public SerReqSaveBankAccountDto toSerReqSaveBankAccountDto() {
        return SerReqSaveBankAccountDto.builder()
                .accountNumber(this.accountNumber)
                .name(this.name)
                .balance(this.balance)
                .id(this.id)
                .build();
    }


}

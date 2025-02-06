package com.account.pro.bankService.controller;

import com.account.pro.bankService.controller.req.ConReqDepositAccountDto;
import com.account.pro.bankService.controller.req.ConReqSaveBankAccountDto;
import com.account.pro.bankService.controller.req.ConReqTransformAccountDto;
import com.account.pro.bankService.controller.req.ConReqWithdrawAccountDto;
import com.account.pro.bankService.repository.entity.BankAccount;
import com.account.pro.bankService.service.BankAccountService;
import com.account.pro.bankService.service.req.SerReqDepositeAccountDto;
import com.account.pro.bankService.service.req.SerReqSaveBankAccountDto;
import com.account.pro.bankService.service.req.SerReqTransformAccountDto;
import com.account.pro.bankService.service.req.SerReqWithdrawAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    //resDto 변경 예정
    @GetMapping("/getAll")
    public Collection<BankAccount> getAll(){
        return bankAccountService.getAll();
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ConReqSaveBankAccountDto dto){
        SerReqSaveBankAccountDto serReqSaveBankAccountDto = dto.toSerReqSaveBankAccountDto();
        bankAccountService.save(serReqSaveBankAccountDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("save success");
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposite(@RequestBody ConReqDepositAccountDto dto){
        SerReqDepositeAccountDto serReqDepositeAccountDto = dto.toSerReqDepositeAccountDto();
        bankAccountService.deposit(serReqDepositeAccountDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body("deposit success");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody ConReqWithdrawAccountDto dto){
        SerReqWithdrawAccountDto serReqWithdrawAccountDto = dto.toSerReqWithdrawAccountDto();
        bankAccountService.withdraw(serReqWithdrawAccountDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body("withdraw success");
    }

    @PostMapping("/transform")
    public ResponseEntity<String> transform(@RequestBody ConReqTransformAccountDto dto){
        SerReqTransformAccountDto serReqTransformAccountDto = dto.toSerReqTransformAccountDto();
        bankAccountService.transform(serReqTransformAccountDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body("transfrom success");
    }


}

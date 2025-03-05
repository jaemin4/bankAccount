package com.pro.bankService.controller;

import com.pro.bankService.controller.request.BankAccountDepositParam;
import com.pro.bankService.controller.request.BankAccountSaveParam;
import com.pro.bankService.controller.request.BankAccountTransferParam;
import com.pro.bankService.controller.request.BankAccountWithdrawParam;
import com.pro.bankService.service.BankService;
import com.pro.response.RestResult;
import com.pro.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user/bank")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankService bankService;

    @GetMapping("/getAll")
    public RestResult getAll(){
        return bankService.getAll();
    }

    @PostMapping("/save")
    public RestResult save(@RequestBody BankAccountSaveParam param){
        param.setUser_id(ServiceUtil.createUserId());
        param.setRole("ROLE_USER");

        return bankService.save(param);
    }

    @PostMapping("/deposit")
    public RestResult deposit(@RequestBody BankAccountDepositParam param){
       // log.info("/bank/save : {}", param);

        return bankService.deposit(param);
    }

    @PostMapping("/withdraw")
    public RestResult withdraw(@RequestBody BankAccountWithdrawParam param){
       // log.info("/bank/withdraw : {}", param);
        RestResult result = new RestResult();
        return bankService.withdraw(param);
    }

    @PostMapping("/transfer")
    public RestResult transfer(@RequestBody BankAccountTransferParam param){
       // log.info("/bank/transfer : {}", param);
        return bankService.transfer(param);

    }


}

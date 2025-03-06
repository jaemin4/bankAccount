package com.pro.feature.bankService.controller;

import com.pro.feature.bankService.controller.request.BankAccountDepositParam;
import com.pro.feature.bankService.controller.request.BankAccountSaveParam;
import com.pro.feature.bankService.controller.request.BankAccountTransferParam;
import com.pro.feature.bankService.controller.request.BankAccountWithdrawParam;
import com.pro.feature.bankService.service.BankService;
import com.pro.response.RestResult;
import com.pro.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        String username = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        log.info("현재 사용자 : {},{}",role,username);
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

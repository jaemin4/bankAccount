package com.account.pro.bankService.controller;

import com.account.pro.bankService.controller.req.BankAccountDepositParam;
import com.account.pro.bankService.controller.req.BankAccountSaveParam;
import com.account.pro.bankService.controller.req.BankAccountTransferParam;
import com.account.pro.bankService.controller.req.BankAccountWithdrawParam;
import com.account.pro.bankService.controller.res.RestError;
import com.account.pro.bankService.controller.res.RestResult;
import com.account.pro.bankService.service.BankFrontService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Profile("health-api")
@Slf4j
@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public RestResult health(){
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("status", "UP");
        return new RestResult(data);
    }

}

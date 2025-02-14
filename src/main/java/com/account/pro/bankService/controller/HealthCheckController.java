package com.account.pro.bankService.controller;

import com.account.pro.bankService.controller.res.RestResult;
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

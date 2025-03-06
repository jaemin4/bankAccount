package com.pro.feature.bankService.controller;

import com.pro.response.RestMessage;
import com.pro.response.RestResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @GetMapping("/access")
    public RestMessage access(){

        return new RestMessage("ROLE_ADMIN");
    }
}

package com.pro.controller;

import com.pro.model.result.RestMessage;
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

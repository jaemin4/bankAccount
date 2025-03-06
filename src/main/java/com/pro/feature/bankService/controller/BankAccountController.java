package com.pro.feature.bankService.controller;

import com.pro.feature.bankService.controller.request.BankAccountDepositParam;
import com.pro.feature.bankService.controller.request.BankAccountSaveParam;
import com.pro.feature.bankService.controller.request.BankAccountTransferParam;
import com.pro.feature.bankService.controller.request.BankAccountWithdrawParam;
import com.pro.feature.bankService.service.BankService;
import com.pro.response.RestResult;
import com.pro.util.JwtUtil;
import com.pro.util.ServiceUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/user/bank")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankService bankService;
    private final JwtUtil jwtUtil;

    @GetMapping("/getAll")
    public RestResult getAll(){
        return bankService.getAll();
    }

    @PostMapping("/save")
    public RestResult save(@RequestBody BankAccountSaveParam param){
        param.setUser_id(ServiceUtil.createUserId());
        param.setRole("ROLE_ADMIN");

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
    public RestResult withdraw(@RequestBody BankAccountWithdrawParam param, HttpServletRequest request){
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities();

        String role = authorities.stream()
                .map(GrantedAuthority::getAuthority)  // "ROLE_ADMIN" 같은 값만 추출
                .collect(Collectors.joining(", ")); // 여러 개의 ROLE이 있을 경우 ", "로 연결

        RestResult restResult = bankService.withdraw(param);
        Map<String,Object> map = new HashMap<>();
        map.put("Security ROLE",role);

        String token = request.getHeader("Authorization");
        if(token != null){
            token = token.split(" ")[1];
            map.put("JWT ROLE",jwtUtil.getRole(token));
        }
        restResult.setData(map);


        return restResult;
    }

    @PostMapping("/transfer")
    public RestResult transfer(@RequestBody BankAccountTransferParam param){
       // log.info("/bank/transfer : {}", param);
        return bankService.transfer(param);

    }


}

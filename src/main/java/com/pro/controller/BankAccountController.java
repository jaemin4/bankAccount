package com.pro.controller;

import com.pro.model.param.BankAccountDepositParam;
import com.pro.model.param.BankAccountSaveParam;
import com.pro.model.param.BankAccountTransferParam;
import com.pro.model.param.BankAccountWithdrawParam;
import com.pro.service.front.BankFrontService;
import com.pro.service.persist.BankService;
import com.pro.model.result.RestResult;
import com.pro.util.JwtUtil;
import com.pro.util.ServiceUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pro.util.Utils.toJson;

@Slf4j
@RestController
@RequestMapping("/user/bank")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankService bankService;
    private final BankFrontService bankFrontService;
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
        log.info("/deposit : {}", toJson(param));

        // todo: ArgumentResolver 로 뺄수 있어요.
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        String username = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        log.info("현재 사용자 : {},{}",role,username);
        return bankFrontService.deposit(param);
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

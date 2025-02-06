package com.account.pro.bankService.controller;

import com.account.pro.bankService.controller.req.BankAccountParam;
import com.account.pro.bankService.controller.res.RestResult;
import com.account.pro.bankService.service.BankAccountService;
import com.account.pro.bankService.service.req.SerReqSaveBankAccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankFrontService {

    private final BankAccountService bankAccountService;

    public RestResult save(BankAccountParam param) {

        SerReqSaveBankAccountDto dto = new SerReqSaveBankAccountDto();
        dto.setAccountNumber(param.getAccountNumber());
        dto.setName(param.getName());
        dto.setBalance(param.getBalance());
        dto.setId(param.getId());

        bankAccountService.save(dto);

        // todo : tx 후 로그 필수자리

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("result", "success");
        data.put("result", "success");
        data.put("result", "success");

        return new RestResult(data);
    }
}

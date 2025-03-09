package com.pro.service.front;

import com.pro.model.entity.AccountEntity;
import com.pro.model.param.BalanceLogParam;
import com.pro.model.param.BankAccountDepositParam;
import com.pro.model.result.RestResult;
import com.pro.service.persist.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

// Aggregator 패턴.
@Slf4j
@Service
@RequiredArgsConstructor
public class BankFrontService {

    private final BankService bankService;
//    private final RabbitService rabbitService;

    @Transactional
    public RestResult deposit(BankAccountDepositParam param) {
        if(param.getBalance() == null || param.getAccountNumber() == null){
            return new RestResult("입금 실패 : 계좌 번호가 없습니다.", "false");
        }

        final AccountEntity savedAccount = bankService.getBankAccount(param.getAccountNumber());

        BalanceLogParam balanceLogParam = new BalanceLogParam();

        String fullMethodName = this.getClass().getSimpleName() + "." +
                                new Object() {}.getClass().getEnclosingMethod().getName();
        balanceLogParam.setClassMethod(Map.of("currentMethod",fullMethodName));

        balanceLogParam.setPrevData(new LinkedHashMap<>(Map.of(
            "account_number",savedAccount.getAccount_number(),
            "balance", savedAccount.getBalance()
        )));

        bankService.deposit(param, savedAccount);

        final AccountEntity updatedAccount = bankService.getBankAccount(param.getAccountNumber());

        balanceLogParam.setCurrentData(new LinkedHashMap<>(Map.of(
            "account_number", updatedAccount.getAccount_number(),
            "balance", updatedAccount.getBalance()
        )));

//        rabbitTemplate.convertAndSend("bank.exchange","bank.log.deposit", balanceLogParam);

        return new RestResult("입금 성공", "true");
    }
}

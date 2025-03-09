package com.pro.service.front;

import com.pro.exception.BankRuntimeException;
import com.pro.model.entity.AccountEntity;
import com.pro.model.entity.UserEntity;
import com.pro.model.param.*;
import com.pro.model.result.RestResult;
import com.pro.service.persist.BankService;
import com.pro.util.ServiceUtil;
import com.pro.util.ValidationChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.pro.util.ValidationChecker.*;

// Aggregator 패턴.
@Slf4j
@Service
@RequiredArgsConstructor
public class BankFrontService {

    private final BankService bankService;
//    private final RabbitService rabbitService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public RestResult saveUserAccount(UserAccountSaveParam param){
        ValidationChecker.userAccountSaveValidationCheck(param);
        param.setRole("ROLE_ADMIN");

        UserEntity userEntity = new UserEntity(
                ServiceUtil.createUserId(),
                param.getName(),
                param.getRole(),
                param.getEmail(),
                bCryptPasswordEncoder.encode(param.getPassword())
        );

        bankService.userSave(userEntity);
        return new RestResult("회원가입 성공", "success");
    }

    public RestResult deposit(BankAccountDepositParam param) {
        ValidationChecker.depositValidationCheck(param);

        final AccountEntity savedAccount = bankService.getBankAccount(param.getAccountNumber());
        BalanceLogParam balanceLogParam = new BalanceLogParam();

        String fullMethodName = this.getClass().getSimpleName() + "." +
                                new Object() {}.getClass().getEnclosingMethod().getName();
        balanceLogParam.setClassMethod(Map.of("currentMethod",fullMethodName));

        balanceLogParam.setSavedAccount(Map.of(
            "accountNumber",savedAccount.getAccount_number(),
            "balance", savedAccount.getBalance()
        ));

        bankService.deposit(param, savedAccount);

        final AccountEntity updatedAccount = bankService.getBankAccount(param.getAccountNumber());

        balanceLogParam.setUpdatedAccount(Map.of(
            "account_number", updatedAccount.getAccount_number(),
            "balance", updatedAccount.getBalance()
        ));

        //rabbitTemplate.convertAndSend("bank.exchange","bank.log.deposit", balanceLogParam);
        return new RestResult("입금 성공", "success");
    }

    public RestResult withdraw(BankAccountWithdrawParam param){
        withrdrawValidationCheck(param);

        final AccountEntity savedAccount = bankService.getBankAccount(param.getAccountNumber());

        if(savedAccount.getBalance() - param.getBalance() <= 0){
            throw new BankRuntimeException("잔액이 부족합니다");
        }

        final BalanceLogParam balanceLogParam = new BalanceLogParam();

        String fullMethodName = this.getClass().getSimpleName() + "." +
                new Object() {}.getClass().getEnclosingMethod().getName();
        balanceLogParam.setClassMethod(Map.of("currentMethod",fullMethodName));

        balanceLogParam.setSavedAccount(Map.of(
                "accountNumber",savedAccount.getAccount_number(),
                "balance", savedAccount.getBalance()
        ));

        bankService.withdraw(param, savedAccount);

        final AccountEntity updatedAccount = bankService.getBankAccount(param.getAccountNumber());

        balanceLogParam.setUpdatedAccount(Map.of(
                "account_number", updatedAccount.getAccount_number(),
                "balance", updatedAccount.getBalance()
        ));

        //rabbitTemplate.convertAndSend("bank.exchange","bank.log.withdraw", balanceLogParam);
        return new RestResult("출금 성공", "success");

    }

    public RestResult transfer(BankAccountTransferParam param){
        transferValidationCheck(param);

        final AccountEntity fromSavedAccount = bankService.getBankAccount(param.getFromAccountNumber());

        if(fromSavedAccount.getBalance() <= param.getBalance()){
            throw new RuntimeException("잔액이 부족합니다");
        }

        final AccountEntity toSavedAccount = bankService.getBankAccount(param.getToAccountNumber());
        final BalanceLogParam balanceLogParam = new BalanceLogParam();

        String fullMethodName = this.getClass().getSimpleName() + "." +
                new Object() {}.getClass().getEnclosingMethod().getName();
        balanceLogParam.setClassMethod(Map.of("currentMethod",fullMethodName));

        balanceLogParam.setSavedAccount(
                Map.of(
                        "fromAccountNumber",fromSavedAccount.getAccount_number(),
                        "fromBalance",fromSavedAccount.getBalance(),
                        "toAccountNumber",toSavedAccount.getAccount_number(),
                        "toBalance",toSavedAccount.getBalance()
                )
        );

        bankService.transfer(param,fromSavedAccount,toSavedAccount);

        //rabbitTemplate.convertAndSend("bank.exchange","bank.log.transfer", balanceLogParam);
        return new RestResult("이체 성공","success");
    }


}

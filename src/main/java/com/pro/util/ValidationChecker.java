package com.pro.util;

import com.pro.exception.BankRuntimeException;
import com.pro.exception.UserRuntimeException;
import com.pro.model.param.BankAccountDepositParam;
import com.pro.model.param.BankAccountTransferParam;
import com.pro.model.param.BankAccountWithdrawParam;
import com.pro.model.entity.AccountEntity;
import com.pro.model.param.UserAccountSaveParam;
import com.pro.model.result.RestResult;

public class ValidationChecker {

    public static void userAccountSaveValidationCheck(UserAccountSaveParam param){
        if(param.getBalance() == null || param.getEmail() == null || param.getPassword() == null || param.getName() == null){
            throw new UserRuntimeException("회원가입 실패");
        }
    }
    public static void depositValidationCheck(BankAccountDepositParam param) {
        if(param.getBalance() == null || param.getAccountNumber() == null){
            throw new BankRuntimeException("입금 실패 : 계좌 번호가 없습니다.");
        }

    }

    public static void withrdrawValidationCheck(BankAccountWithdrawParam param) {
        if(param.getBalance() == null || param.getAccountNumber() == null){
            throw new BankRuntimeException("출금 실패 : 계좌 번호가 없습니다.");
        }

    }


    public static void transferValidationCheck(BankAccountTransferParam param) {
        if (param.getBalance() == null || param.getFromAccountNumber() == null || param.getToAccountNumber() == null) {
            throw new BankRuntimeException("이체 실패 : 계좌 번호가 없습니다.");
        }

    }
}

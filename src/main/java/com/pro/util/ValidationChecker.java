package com.pro.util;

import com.pro.exception.BankRuntimeException;
import com.pro.model.param.BankAccountTransferParam;
import com.pro.model.param.BankAccountWithdrawParam;
import com.pro.model.entity.AccountEntity;

public class ValidationChecker {


    public static void withrdrawValidationCheck(BankAccountWithdrawParam param, AccountEntity prevAccount) {
        if(param.getBalance() == null || param.getAccount_number() == null){
            throw new BankRuntimeException("출금 실패 : 계좌 번호가 없습니다.");
        }

        if (prevAccount == null) {
            throw new BankRuntimeException("출금 실패: 기존 계좌 정보를 찾을 수 없습니다.");
        }

        if (prevAccount.getBalance() < param.getBalance()) {
            throw new BankRuntimeException("출금 실패: 잔액 부족");
        }
    }

    public static void transferValidationCheck(BankAccountTransferParam param) {
        if (param.getBalance() == null || param.getFromAccountNumber() == null || param.getTo_account_number() == null) {
            throw new BankRuntimeException("이체 실패 : 계좌 번호가 없습니다.");
        }
        if (param.getBalance() <= 0) {
            throw new BankRuntimeException("이체 실패 : 이체 금액을 확인해주세요.");
        }
    }
}

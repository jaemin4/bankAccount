package com.account.pro.bankService.service;

import com.account.pro.bankService.controller.request.BankAccountDepositParam;
import com.account.pro.bankService.controller.request.BankAccountSaveParam;
import com.account.pro.bankService.controller.request.BankAccountTransferParam;
import com.account.pro.bankService.controller.request.BankAccountWithdrawParam;
import com.account.pro.bankService.service.response.RestResult;

public interface BankInterfaceService {
    RestResult getAll();

    RestResult save(BankAccountSaveParam param);

    RestResult deposit(BankAccountDepositParam param);

    RestResult withdraw(BankAccountWithdrawParam param);

    RestResult transfer(BankAccountTransferParam param);
}

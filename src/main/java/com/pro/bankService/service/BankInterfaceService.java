package com.pro.bankService.service;

import com.pro.bankService.controller.request.BankAccountDepositParam;
import com.pro.bankService.controller.request.BankAccountSaveParam;
import com.pro.bankService.controller.request.BankAccountTransferParam;
import com.pro.bankService.controller.request.BankAccountWithdrawParam;
import com.pro.bankService.service.response.RestResult;

public interface BankInterfaceService {
    RestResult getAll();

    RestResult save(BankAccountSaveParam param);

    RestResult deposit(BankAccountDepositParam param);

    RestResult withdraw(BankAccountWithdrawParam param);

    RestResult transfer(BankAccountTransferParam param);
}

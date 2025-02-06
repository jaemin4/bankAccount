package com.account.pro.bankService.memory;

import com.account.pro.bankService.repository.entity.BankAccount;

import java.util.HashMap;
import java.util.Map;

public class BankAccountMemory {

    private static final  Map<Long, BankAccount> accountMap = new HashMap<>();

    public static Map<Long, BankAccount> getAccountMap() {
        return accountMap;
    }

}

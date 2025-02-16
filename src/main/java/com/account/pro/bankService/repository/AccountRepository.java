package com.account.pro.bankService.repository;

import com.account.pro.bankService.repository.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
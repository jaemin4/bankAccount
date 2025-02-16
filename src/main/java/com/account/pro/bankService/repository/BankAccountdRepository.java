package com.account.pro.bankService.repository;

import com.account.pro.bankService.repository.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountdRepository extends JpaRepository<BankAccount, Long> {
}
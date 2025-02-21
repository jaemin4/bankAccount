package com.pro.bankService.repository;

import com.pro.bankService.repository.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountdRepository extends JpaRepository<BankAccount, Long> {
}
package com.account.pro.bankService.repository;

import com.account.pro.bankService.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
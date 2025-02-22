package com.pro.bankService.repository.mybatis;

import com.pro.bankService.repository.mybatis.entity.BankAccountEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BankAccountRepository {

    Integer save(BankAccountEntity bankAccountEntity);
    BankAccountEntity getAll();



}

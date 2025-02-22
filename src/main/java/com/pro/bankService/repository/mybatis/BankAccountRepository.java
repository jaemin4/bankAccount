package com.pro.bankService.repository.mybatis;

import com.pro.bankService.repository.mybatis.entity.BankAccountEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BankAccountRepository {

    Integer save(BankAccountEntity bankAccountEntity);
    List<BankAccountEntity> getAll();



}

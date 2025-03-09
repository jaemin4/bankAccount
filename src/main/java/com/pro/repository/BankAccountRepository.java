package com.pro.repository;

import com.pro.model.entity.BankAccountEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BankAccountRepository {

    Integer save(BankAccountEntity bankAccountEntity);
    List<BankAccountEntity> getAll();



}

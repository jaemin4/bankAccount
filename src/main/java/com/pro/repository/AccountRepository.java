package com.pro.repository;

import com.pro.model.entity.AccountEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface AccountRepository {
    Long save(AccountEntity accountEntity);
    Optional<AccountEntity> findById(Long accountNumber);

    Integer updateAccountBalance(AccountEntity accountEntity);

}

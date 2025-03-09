package com.pro.repository;

import com.pro.model.entity.AccountEntity;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface AccountRepository {
    Long save(AccountEntity accountEntity);
    AccountEntity findById(Long accountNumber);

    Integer updateAccountBalance(AccountEntity accountEntity);

}

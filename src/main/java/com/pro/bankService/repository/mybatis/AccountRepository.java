package com.pro.bankService.repository.mybatis;

import com.pro.bankService.repository.mybatis.entity.AccountEntity;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface AccountRepository {
    Long save(AccountEntity accountEntity);
    AccountEntity findById(AccountEntity accountEntity);

    Integer updateAccountBalance(AccountEntity accountEntity);

}

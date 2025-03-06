package com.pro.feature.bankService.repository.mybatis;

import com.pro.feature.bankService.repository.mybatis.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {


    Integer save(UserEntity userEntity);

    UserEntity findByUsername(String username);



}

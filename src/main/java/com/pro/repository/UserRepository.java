package com.pro.repository;

import com.pro.model.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {


    Integer save(UserEntity userEntity);

    UserEntity findByUsername(String username);



}

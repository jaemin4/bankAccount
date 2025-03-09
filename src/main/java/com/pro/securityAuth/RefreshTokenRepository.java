package com.pro.securityAuth;


import com.pro.securityAuth.RefreshEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

@Mapper
public interface RefreshTokenRepository {
    void saveRefresh(RefreshEntity refreshEntity);
    Boolean existRefresh(@Param("refresh_token") String refresh_token);

    @Transactional
    void deleteByRefresh(@Param("refresh_token") String refresh_token);
}

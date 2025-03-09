package com.pro.util;

import com.pro.model.entity.UserEntity;
import com.pro.model.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FilterUtil {

    public void setContextHodler(String username, String role){
        log.info("set : {}, role : {}", username, role);

        UserEntity userEntity = new UserEntity(
                username,
                role
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                "",
                customUserDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }



}

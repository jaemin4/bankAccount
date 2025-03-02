package com.pro.securityAuth;


import com.pro.bankService.repository.mybatis.UserRepository;
import com.pro.bankService.repository.mybatis.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUerBy : {}",username);
        UserEntity entity = userRepository.findByUsername(username);
        if (entity != null) {
            return new CustomUserDetails(entity);
        }
        return null;
    }
}

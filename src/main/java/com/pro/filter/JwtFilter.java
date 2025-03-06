package com.pro.filter;

import com.pro.feature.bankService.repository.mybatis.entity.UserEntity;
import com.pro.securityAuth.CustomUserDetails;
import com.pro.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = request.getHeader("Authorization");
        log.info("jwt : " + authToken);
        if(authToken == null || !authToken.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        String token = authToken.split(" ")[1];
        if(jwtUtil.isExpired(token)){
            filterChain.doFilter(request, response);
            return;
        }


        UserEntity userEntity = new UserEntity(
                jwtUtil.getUsername(token),
                jwtUtil.getRole(token)
        );

        log.info("username : {}, role : {}", jwtUtil.getUsername(token), jwtUtil.getRole(token));

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                "",
                customUserDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}

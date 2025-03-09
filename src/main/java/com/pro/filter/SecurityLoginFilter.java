package com.pro.filter;

import com.pro.repository.RefreshTokenRepository;
import com.pro.model.entity.RefreshEntity;
import com.pro.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.util.Date;

@Slf4j
public class SecurityLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public SecurityLoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository){
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        setFilterProcessesUrl("/auth/login");
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                obtainUsername(request),
                obtainPassword(request),
                null
        );
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = authResult.getName();
        String role = authResult.getAuthorities().iterator().next().getAuthority();

        String accessToken = jwtUtil.createJwt("access", username, role, jwtUtil.getAccessExpiredMs());
        String refreshToken = jwtUtil.createJwt("refresh", username, role, jwtUtil.getRefreshExpiredMs());

        SecurityContextHolder.getContext().setAuthentication(authResult);
        log.info("JWT :  " + accessToken);

        RefreshEntity refreshEntity = new RefreshEntity(
                username,
                refreshToken,
                new Date(System.currentTimeMillis() + jwtUtil.getRefreshExpiredMs()).toString(),
                accessToken,
                new Date(System.currentTimeMillis() + jwtUtil.getAccessExpiredMs()).toString()
        );
        refreshTokenRepository.saveRefresh(refreshEntity);

        response.setHeader("access", accessToken);
        response.addCookie(jwtUtil.createCookie(refreshToken));
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }



}

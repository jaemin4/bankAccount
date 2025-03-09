package com.pro.securityAuth;

import com.pro.response.RestResult;
import com.pro.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/reissue")
    public RestResult accessTokenRessiue(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();

        String refresh = null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
                break;
            }
        }

        if(refresh == null){
            return new RestResult("refresh token null","false");
        }

        try {
            jwtUtil.isExpired(refresh);
        }catch (ExpiredJwtException e){
            return new RestResult("access token expired", "false");
        }

        if(!jwtUtil.getCategory(refresh).equals("refresh")){
            return new RestResult("invalid refresh token", "false");
        }

        Boolean isExistRefresh = refreshTokenRepository.existRefresh(refresh);
        if(!isExistRefresh){
            return new RestResult("invalid refresh token", "false");
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createJwt("access",username,role,600000L);
        String newRefresh = jwtUtil.createJwt("refresh",username,role,86400000L);

        refreshTokenRepository.deleteByRefresh(refresh);
        RefreshEntity refreshEntity = new RefreshEntity(
                username,
                refresh,
                new Date(System.currentTimeMillis() + jwtUtil.getRefreshExpiredMs()).toString(),
                newAccess,
                new Date(System.currentTimeMillis() + jwtUtil.getAccessExpiredMs()).toString()
        );

        refreshTokenRepository.saveRefresh(refreshEntity);

        response.setHeader("access",newAccess);
        response.addCookie(jwtUtil.createCookie(newRefresh));
        return new RestResult("access token reissue success","success");
    }


}

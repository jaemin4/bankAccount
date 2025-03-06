package com.pro.util;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jdk.jfr.Category;
import lombok.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(){
        String secret = "vmfhaltmskdlstkfkdgodyroqkfwkdbalroqkfwkdbalaaaaaaaaaaaaaaaabbbbb";
        secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }
    public String getUsername(String token){
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("username",String.class);
    }
    public String getRole(String token){
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("role",String.class);
    }
    public Boolean isExpired(String token){
        return Jwts.parser().verifyWith(secretKey).build().
                parseSignedClaims(token).getPayload().
                getExpiration().before(new Date());
    }
    public String getCategory(String token){
        return Jwts.parser().verifyWith(secretKey).build().
                parseSignedClaims(token).getPayload().
                get("category",String.class);
    }
    public Long getAccessExpiredMs(){
        return 600000L;
    }
    public Long getRefreshExpiredMs(){
        return 86400000L;
    }

    public String createJwt(String category,String username, String role, Long expiredMs){
        return Jwts.builder()
                .claim("category",category)
                .claim("username",username)
                .claim("role",role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public Cookie createCookie(String value){
        Cookie cookie = new Cookie("refresh",value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }




}

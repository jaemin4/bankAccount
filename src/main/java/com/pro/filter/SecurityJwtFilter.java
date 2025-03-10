package com.pro.filter;

import com.pro.util.FilterUtil;
import com.pro.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@AllArgsConstructor
public class SecurityJwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final FilterUtil filterUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("Authorization");
        if(accessToken == null){
            filterChain.doFilter(request,response);
            return;
        }




        try {
            jwtUtil.isExpired(accessToken);
        }catch (ExpiredJwtException e){

            // todo 여기를 포함해서 다른곳에서도 예외로그나 혹은 실패로그를 남길때는 로그 레벨이 warn 이상으로 남기는 것이 좋습니다.
            log.info("access token expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(accessToken);

        // todo 코드레벨에 관한 이야기입니다. 여기 있는 로직이 jwtUtil 에 있는게 좋을것 같아요. 왜냐면 isExpired 라는 메소드가 이미 있어서 그것과 코드레벨을 맞추는게 좋을것 같습니다.
        if(!category.equals("access")){
            log.info("invaild access token");
        }

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        filterUtil.setContextHodler(username, role);

        filterChain.doFilter(request,response);
    }
}

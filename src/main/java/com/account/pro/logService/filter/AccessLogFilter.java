package com.account.pro.logService.filter;

import com.account.pro.logService.service.AccessLogService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessLogFilter implements Filter {

    private final AccessLogService accessLogService;

    /**
     * AccessLog 필터링 및 로깅 처리를 담당하는 메서드.
     * "access" 프로파일이 활성화된 경우 필터를 건너뛰며,
     * `access-log.enabled` 설정에 따라 로깅 여부를 결정한다.
     *  second -> millisecond -> microsecond -> nanosecond
     *
     * @param request  클라이언트의 요청 객체
     * @param response 서버의 응답 객체
     * @param chain    다음 필터 또는 서블릿으로 요청을 전달하기 위한 필터 체인
     * @throws IOException   입출력 예외 발생 시
     * @throws ServletException 서블릿 예외 발생 시
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        LocalDateTime requestAt = LocalDateTime.now();
        chain.doFilter(requestWrapper,responseWrapper);
        LocalDateTime responseAt = LocalDateTime.now();

        log.info("AccessLogService Save : {}",accessLogService.saveAccessLog(requestWrapper,responseWrapper,requestAt,responseAt));
        accessLogService.printReqAccessLog(requestWrapper,requestAt);
        accessLogService.printResAccessLog(responseWrapper,requestAt,responseAt);

        responseWrapper.copyBodyToResponse();

    }
}
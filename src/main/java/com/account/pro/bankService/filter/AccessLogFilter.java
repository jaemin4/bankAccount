package com.account.pro.bankService.filter;

import com.account.pro.bankService.filter.request.AccessReqLog;
import com.account.pro.bankService.filter.response.AccessResLog;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessLogFilter implements Filter {

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

        AccessReqLog reqLog = new AccessReqLog(requestWrapper);
        reqLog.logRequest();

        chain.doFilter(requestWrapper, responseWrapper);

        AccessResLog resLog = new AccessResLog(responseWrapper,reqLog);
        resLog.logResponse();

        // todo: 실제 응답이 나가지 않고 있습니다. (브라우저에서 /bank/getAll 로 확인해보시면 됩니다.)
        // todo: 필터 체인을 통과한 후에는 반드시 응답 본문을 다시 복사해 주도록 해주세요.
    }
}
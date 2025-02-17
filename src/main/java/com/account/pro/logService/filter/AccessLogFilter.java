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

        // TPS (Transaction Per Second) : 1초당 처리량 --> 1000 TPS X 1분정도 지속되면, 1분에 60,000 번 인 경우에는 ? DB CPU 는 100% 향해 갑니다.
        // 비동기 처리를 하면 가장 확실한 방법입니다.
        // fire & forget 방식 : 비동기 방식으로 처리하면, 응답을 기다리지 않고, 바로 다음 작업을 수행합니다.
        // todo : RabbitMQ 로 보내서 저장하도록 해주세요. 컨슈머를 하나 만들어서 저장하면 됩니다.
        log.info("AccessLogService Save : {}", accessLogService.saveAccessLog(requestWrapper,responseWrapper,requestAt,responseAt));

        accessLogService.printReqAccessLog(requestWrapper,requestAt);
        accessLogService.printResAccessLog(responseWrapper,requestAt,responseAt);

        responseWrapper.copyBodyToResponse();

    }
}
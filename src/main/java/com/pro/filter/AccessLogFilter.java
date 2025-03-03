package com.pro.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pro.logService.AccessLogParam;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessLogFilter implements Filter {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper res = new ContentCachingResponseWrapper((HttpServletResponse) response);
        LocalDateTime requestAt = LocalDateTime.now();


        chain.doFilter(req,res);

        LocalDateTime responseAt = LocalDateTime.now();

        AccessLogParam accessLogParam = new AccessLogParam(
                req.getMethod(),
                req.getRequestURI(),
                req.getQueryString(),
                objectMapper.writeValueAsString(req.getParameterMap()),
                req.getReader().lines().reduce("", (acc, line) -> acc + line),
                Collections.list(req.getHeaderNames()).stream()
                        .collect(Collectors.toMap(h -> h, req::getHeader))
                        .toString(),
                req.getHeader("User-Agent"),
                req.getHeader("Referer"),
                Optional.ofNullable(req.getHeader("X-Forwarded-For")).orElse(req.getRemoteAddr()),
                req.getHeader("Host"),
                req.getHeader("Authorization"),
                requestAt,
                Thread.currentThread().getName(),
                responseAt,
                new String(res.getContentAsByteArray()),
                res.getStatus() >= 400 ? "ERROR" : "SUCCESS",
                res.getStatus(),
                Duration.between(requestAt, responseAt).toMillis()
        );

        rabbitTemplate.convertAndSend("bank.exchange","bank.log.access", accessLogParam);

        res.copyBodyToResponse();

    }



}
package com.account.pro.bankService.filter.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class AccessReqLog {

    // HTTP 메서드 (GET, POST 등)
    private String method;
    // 요청 쿼리 파라미터
    private String uri;

    //요청 쿼리 스트링
    private String queryString;

    // 요청 쿼리 파라미터
    private String queryParams;

    // 요청 바디
    private String requestBody;

    // 요청 헤더
    private String headers;

    // User-Agent
    private String userAgent;

    // Referer 헤더
    private String referer;

    // 클라이언트 IP (X-Forwarded-For 고려)
    private String clientIp;

    // Host 정보
    private String host;

    // Authorization
    private String authorization;

    // 요청 시간
    private LocalDateTime requestAt;

    // 요청을 처리하는 스레드 ID
    private String threadId;

    public AccessReqLog(HttpServletRequest req) throws IOException {
        this.method = req.getMethod();
        this.uri = req.getRequestURI();
        this.queryString = req.getQueryString();
        this.userAgent = req.getHeader("User-Agent");
        this.referer = req.getHeader("Referer");
        this.clientIp = Optional.ofNullable(req.getHeader("X-Forwarded-For")).orElse(req.getRemoteAddr());
        this.host = req.getHeader("Host");
        this.authorization = req.getHeader("Authorization");
        this.requestAt = LocalDateTime.now();
        this.threadId = Thread.currentThread().getName();
        this.requestBody = req.getReader().lines().reduce("", (acc, line) -> acc + line);

        this.queryParams = new ObjectMapper().writeValueAsString(
                req.getParameterMap().entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> String.join(",", e.getValue())))
        );

        this.headers = new ObjectMapper().writeValueAsString(
                Collections.list(req.getHeaderNames()).stream()
                        .collect(Collectors.toMap(header -> header, req::getHeader))
        );
    }

    private String getQueryParams(HttpServletRequest req) {
        Map<String, String[]> paramMap = req.getParameterMap();
        Map<String, String> queryParams = new HashMap<>();

        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            queryParams.put(entry.getKey(), String.join(",", entry.getValue()));
        }

        try {
            return new ObjectMapper().writeValueAsString(queryParams);
        } catch (JsonProcessingException e) {
            log.error("⚠ Query Parameters 변환 오류", e);
            return "{}";
        }
    }

    public void logRequest() {
        Map<String, Object> logData = new LinkedHashMap<>();
            logData.put("Method", method);
            logData.put("URI", uri);
            logData.put("Query Params", queryParams);
            logData.put("Query String",queryString);
            logData.put("Request Body", requestBody);
            logData.put("Headers", headers);
            logData.put("User-Agent", userAgent);
            logData.put("Referer", referer);
            logData.put("Client IP", clientIp);
            logData.put("Host", host);
            logData.put("Authorization", authorization);
            logData.put("Request At", requestAt);
            logData.put("Thread ID", threadId);

        String logMessage = logData.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .map(entry -> "➡ " + entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));

        log.info("\n🔹 AccessRequestLog\n{}", logMessage);
    }




}

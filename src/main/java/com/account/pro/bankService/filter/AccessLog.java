package com.account.pro.bankService.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class AccessLog {

    private Long seq;
    private String memberId;
    private String email;
    private String threadId;
    private String host;
    private String authorization;
    private String method;
    private String uri;
    private String service;
    private String os;
    private String deviceClass;
    private String agentName;   // user-agent 에서 추출한 agent name
    private String agentClass;  // user-agent 에서 추출한 agent class
    private String clientIp;    // client ip
    private String country;     // client ip 에서 추출한 country
    private String city;        // client ip 에서 추출한 city
    private long elapsed;       // 요청부터 응답까지 걸린 시간 (ms)
    private String request;     // todo request parameter 와 request body 에 있는 데이터를 합쳐서 저장
    private String response;    // todo response body 에 있는 데이터를 저장
    private String status;      // 200, 400, 500 등
    private String deviceName;  // user-agent 에서 추출한 device name
    private String osName;      // user-agent 에서 추출한 os name
    private String osVersion;   // user-agent 에서 추출한 os version
    private String userAgent;   // user-agent
    private String referer;     // http request header "referer"
    private String errorId;     // 응답이 에러인 경우 에러 ID
    private LocalDateTime requestAt;    // 요청 시각
    private LocalDateTime responseAt;   // 응답 시각
    private String requestId;

    // 새로운 생성자 (테스트에서 사용)
    public AccessLog(String memberId, String status, String uri) {
        this.memberId = memberId;
        this.status = status;
        this.uri = uri;
        this.requestAt = LocalDateTime.now();
    }

    public AccessLog(String memberId, String uri, String method, String userAgent,
                     String clientIp, String referer, LocalDateTime requestAt,
                     String threadId, String host, String authorization,
                     String queryParams, String headers, String requestBody) {
        this.memberId = memberId;
        this.uri = uri;
        this.method = method;
        this.userAgent = userAgent;
        this.clientIp = clientIp;
        this.referer = referer;
        this.requestAt = requestAt;
        this.threadId = threadId;
        this.host = host;
        this.authorization = authorization;
        this.request = String.format("{ \"queryParams\": %s, \"headers\": %s, \"body\": %s }",
                queryParams, headers, requestBody);
    }



    public AccessLog(String memberId, String uri, String method, String userAgent, String clientIp, String referer, LocalDateTime requestAt, String threadId) {
        this.memberId = memberId;
        this.uri = uri;
        this.method = method;
        this.userAgent = userAgent;
        this.clientIp = clientIp;
        this.referer = referer;
        this.requestAt = requestAt;
        this.threadId = threadId;
    }


    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()); // LocalDateTime 지원

    public void loggingAccessLog(AccessLog logData) {
        if (logData == null) {
            log.warn("⚠ AccessLog 객체가 null입니다.");
            return;
        }

        try {
            String jsonLog = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(logData);
            log.info("🔹 AccessLog Data:\n{}", jsonLog);
        } catch (Exception e) {
            log.error("⚠ AccessLog 변환 오류", e);
        }
    }
}
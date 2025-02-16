package com.account.pro.logService.service;

import com.account.pro.logService.repository.AccessLogEntityRepository;
import com.account.pro.logService.repository.entity.AccessLogEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessLogService {

    private final AccessLogEntityRepository accessLogEntityRepository;

    @Transactional
    public String saveAccessLog(ContentCachingRequestWrapper req, ContentCachingResponseWrapper res,
                                LocalDateTime requestAt, LocalDateTime responseAt) throws IOException {

        AccessLogEntity entity = new AccessLogEntity(
                req.getMethod(),
                req.getRequestURI(),
                req.getQueryString(),
                req.getParameterMap().toString(),
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

        return accessLogEntityRepository.save(entity).getAccessLogId() == null ? "FAIL" : "SUCCESS";
    }

    public void printReqAccessLog(ContentCachingRequestWrapper req, LocalDateTime requestAt) throws IOException {
        Map<String, Object> logData = new LinkedHashMap<>();
            logData.put("Method", req.getMethod());
            logData.put("URI", req.getRequestURI());
            logData.put("Query Params", req.getParameterMap().toString());
            logData.put("Query String", req.getQueryString());
            logData.put("Request Body",  req.getReader().lines().reduce("", (acc, line) -> acc + line));
            logData.put("Headers", Collections.list(req.getHeaderNames()).stream()
                    .collect(Collectors.toMap(h -> h, req::getHeader))
                    .toString());
            logData.put("User-Agent", req.getHeader("User-Agent"));
            logData.put("Referer", req.getHeader("Referer"));
            logData.put("Client IP", Optional.ofNullable(req.getHeader("X-Forwarded-For"))
                    .orElse(req.getRemoteAddr()));
            logData.put("Host", req.getHeader("Host"));
            logData.put("Authorization", req.getHeader("Authorization"));
            logData.put("Request At", requestAt);
            logData.put("Thread ID", Thread.currentThread().getName());

        String logMessage = logData.entrySet().stream()
              .filter(entry -> entry.getValue() != null)
              .map(entry -> "➡ " + entry.getKey() + ": " + entry.getValue())
              .collect(Collectors.joining("\n"));

        log.info("\n🔹 AccessRequestLog\n{}", logMessage);

    }

    public void printResAccessLog(ContentCachingResponseWrapper res,LocalDateTime requestAt,LocalDateTime responseAt) {
        Map<String, Object> logData = new LinkedHashMap<>();
            logData.put("Response At", responseAt);
            logData.put("Status Code", res.getStatus() >= 400 ? "ERROR" : "SUCCESS");
            logData.put("Status", res.getStatus());
            logData.put("Response Body", new String(res.getContentAsByteArray(), StandardCharsets.UTF_8));
            logData.put("Elapsed", Duration.between(requestAt, responseAt).toMillis());

        String logMessage = logData.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .map(entry -> "➡ " + entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));

        log.info("\n🔹 AccessResponseLog\n{}", logMessage);
    }



}

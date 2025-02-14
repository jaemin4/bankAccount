package com.account.pro.bankService.filter.response;

import com.account.pro.bankService.filter.request.AccessReqLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class AccessResLog {

    //body 응답, 성공 응답, 리디렉션 응답, 클라이언트 오류 응답, 서버 오류 응답
    private LocalDateTime responseAt;

    //응답 body
    private String responseBody;

    //응답 Status 에러 or 성공
    private Integer statusCode;

    //Status
    private String status;

    //에러 ID
    private String errorId;

    //응답까지 걸리 시간
    private Long elapsed;

    public AccessResLog(ContentCachingResponseWrapper res, AccessReqLog req) {
        this.responseAt = LocalDateTime.now();
        this.statusCode = res.getStatus();
        this.status = (statusCode >= 400) ? "ERROR_" : "SUCCESS_";
        this.responseBody =  new String(res.getContentAsByteArray(), StandardCharsets.UTF_8);
        this.elapsed = Duration.between(req.getRequestAt(), responseAt).toMillis();
    }

    public void logResponse(){
        Map<String,Object> logData = new LinkedHashMap<>();
        logData.put("ResponseAt", responseAt);
        logData.put("StatusCode", statusCode);
        logData.put("status", status);
        logData.put("responseBody", responseBody);
        logData.put("elapsed", elapsed < 1000 ? elapsed +" ms" : elapsed +" second");

        String logMessage = logData.entrySet().stream()
                .filter(map -> map.getValue() != null)
                .map(entry -> "➡ " + entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));

        log.info("\n🔹 AccessResponseLog\n{}", logMessage);
    }





}

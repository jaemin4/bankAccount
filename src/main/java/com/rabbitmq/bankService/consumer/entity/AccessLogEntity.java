package com.rabbitmq.bankService.consumer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "T_ACCESS_LOG"
)
public class AccessLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AccessLogId;

    @Column(length = 20)
    private String method;

    @Column(length = 200)
    private String uri;

    @Column(length = 2000)
    private String queryString;

    @Column(length = 2000)
    private String queryParams;

    // todo 사이즈가 2000 을 넘어가면 저장이 실패가 됩니다. 어떤 전략들이 가능할까요 ?
    @Column(length = 2000)
    private String requestBody;

    @Column(length = 2000)
    private String headers;

    @Column(length = 200)
    private String userAgent;

    @Column(length = 200)
    private String referer;

    @Column(length = 50)
    private String clientIp;

    @Column(length = 200)
    private String host;

    @Column(length = 255)
    private String authorization;

    @Column
    private LocalDateTime requestAt;

    @Column
    private String threadId;

    @Column
    private LocalDateTime responseAt;

    @Column(length = 2000)
    private String responseBody;

    @Column(length = 20)
    private String status;

    @Column
    private Integer statusCode;

    @Column
    private Long elapsed;


    public AccessLogEntity(String method, String uri, String queryString, String queryParams, String requestBody,
                           String headers, String userAgent, String referer, String clientIp, String host,
                           String authorization, LocalDateTime requestAt, String threadId, LocalDateTime responseAt,
                           String responseBody, String status, Integer statusCode, Long elapsed) {
        this.method = method;
        this.uri = uri;
        this.queryString = queryString;
        this.queryParams = queryParams;
        this.requestBody = requestBody;
        this.headers = headers;
        this.userAgent = userAgent;
        this.referer = referer;
        this.clientIp = clientIp;
        this.host = host;
        this.authorization = authorization;
        this.requestAt = requestAt;
        this.threadId = threadId;
        this.responseAt = responseAt;
        this.responseBody = responseBody;
        this.status = status;
        this.statusCode = statusCode;
        this.elapsed = elapsed;
    }

}

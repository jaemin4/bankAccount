package com.pro.model.entity;

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
    private Long access_log_id;

    @Column(length = 20)
    private String method;

    @Column(length = 200)
    private String uri;

    @Column(length = 2000)
    private String query_string;

    @Column(length = 2000)
    private String query_param;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String request_body;


    @Column(length = 2000)
    private String headers;

    @Column(length = 200)
    private String user_agent;

    @Column(length = 200)
    private String referer;

    @Column(length = 50)
    private String client_ip;

    @Column(length = 200)
    private String host;

    @Column(length = 255)
    private String authorization;

    @Column
    private LocalDateTime request_at;

    @Column
    private String thread_id;

    @Column
    private LocalDateTime response_at;

    @Column(length = 2000)
    private String response_body;

    @Column(length = 20)
    private String status;

    @Column
    private Integer status_code;

    @Column
    private Long elapsed;


    public AccessLogEntity(String method, String uri, String query_string, String query_param, String request_body,
                           String headers, String user_agent, String referer, String client_ip, String host,
                           String authorization, LocalDateTime request_at, String thread_id, LocalDateTime response_at,
                           String response_body, String status, Integer status_code, Long elapsed) {
        this.method = method;
        this.uri = uri;
        this.query_string = query_string;
        this.query_param = query_param;
        this.request_body = request_body;
        this.headers = headers;
        this.user_agent = user_agent;
        this.referer = referer;
        this.client_ip = client_ip;
        this.host = host;
        this.authorization = authorization;
        this.request_at = request_at;
        this.thread_id = thread_id;
        this.response_at = response_at;
        this.response_body = response_body;
        this.status = status;
        this.status_code = status_code;
        this.elapsed = elapsed;
    }

}

package com.pro.model.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessLogParam {
    private Long access_log_id;
    private String method;
    private String uri;
    private String query_string;
    private String query_params;
    private String request_body;
    private String headers;
    private String user_agent;
    private String referer;
    private String client_ip;
    private String host;
    private String authorization;
    private LocalDateTime request_at;
    private String thread_id;
    private LocalDateTime response_at;
    private String response_body;
    private String status;
    private Integer status_code;
    private Long elapsed;

    public AccessLogParam(String method, String uri, String query_string, String query_params, String request_body,
                          String headers, String user_agent, String referer, String client_ip, String host,
                          String authorization, LocalDateTime request_at, String thread_id, LocalDateTime response_at,
                          String response_body, String status, Integer status_code, Long elapsed) {
        this.method = method;
        this.uri = uri;
        this.query_string = query_string;
        this.query_params = query_params;
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
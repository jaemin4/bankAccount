package com.rabbitmq.bankService.consumer.repository;

import com.rabbitmq.bankService.consumer.entity.AccessLogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccessLogJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Batch Insert를 수행하여 성능 최적화
     */
    public void saveAll(List<AccessLogEntity> accessLogEntities) {
        String sql = "INSERT INTO T_ACCESS_LOG (method, uri, query_string, query_param, request_body, " +
                "headers, user_agent, referer, client_ip, host, authorization, request_at, " +
                "thread_id, response_at, response_body, status, status_code, elapsed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                AccessLogEntity log = accessLogEntities.get(i);

                ps.setString(1, log.getMethod() != null ? log.getMethod() : "");
                ps.setString(2, log.getUri() != null ? log.getUri() : "");
                ps.setString(3, log.getQuery_string() != null ? log.getQuery_string() : "");
                ps.setString(4, log.getQuery_param() != null ? log.getQuery_param() : "");
                ps.setString(5, log.getRequest_body() != null ? log.getRequest_body() : "");
                ps.setString(6, log.getHeaders() != null ? log.getHeaders() : "");
                ps.setString(7, log.getUser_agent() != null ? log.getUser_agent() : "");
                ps.setString(8, log.getReferer() != null ? log.getReferer() : "");
                ps.setString(9, log.getClient_ip() != null ? log.getClient_ip() : "");
                ps.setString(10, log.getHost() != null ? log.getHost() : "");
                ps.setString(11, log.getAuthorization() != null ? log.getAuthorization() : "");
                ps.setTimestamp(12, log.getRequest_at() != null ? Timestamp.valueOf(log.getRequest_at()) : null);
                ps.setString(13, log.getThread_id() != null ? log.getThread_id() : "");
                ps.setTimestamp(14, log.getResponse_at() != null ? Timestamp.valueOf(log.getResponse_at()) : null);
                ps.setString(15, log.getResponse_body() != null ? log.getResponse_body() : "");
                ps.setString(16, log.getStatus() != null ? log.getStatus() : "");
                ps.setInt(17, log.getStatus_code() != null ? log.getStatus_code() : 0);
                ps.setLong(18, log.getElapsed() != null ? log.getElapsed() : 0L);
            }

            @Override
            public int getBatchSize() {
                return accessLogEntities.size();
            }
        });
    }
}

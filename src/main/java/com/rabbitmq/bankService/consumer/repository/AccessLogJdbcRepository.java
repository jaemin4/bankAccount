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
        String sql = "INSERT INTO t_access_log (authorization, client_ip, elapsed, headers, host, method, " +
                "query_params, query_string, referer, request_at, request_body, response_at, response_body, " +
                "status, status_code, thread_id, uri, user_agent) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                AccessLogEntity log = accessLogEntities.get(i);
                ps.setString(1, log.getAuthorization());
                ps.setString(2, log.getClientIp());
                ps.setLong(3, log.getElapsed());
                ps.setString(4, log.getHeaders());
                ps.setString(5, log.getHost());
                ps.setString(6, log.getMethod());
                ps.setString(7, log.getQueryParams());
                ps.setString(8, log.getQueryString());
                ps.setString(9, log.getReferer());
                ps.setTimestamp(10, Timestamp.valueOf(log.getRequestAt()));
                ps.setString(11, log.getRequestBody());
                ps.setTimestamp(12, Timestamp.valueOf(log.getResponseAt()));
                ps.setString(13, log.getResponseBody());
                ps.setString(14, log.getStatus());
                ps.setInt(15, log.getStatusCode());
                ps.setString(16, log.getThreadId());
                ps.setString(17, log.getUri());
                ps.setString(18, log.getUserAgent());
            }

            @Override
            public int getBatchSize() {
                return accessLogEntities.size();
            }
        });
    }

}

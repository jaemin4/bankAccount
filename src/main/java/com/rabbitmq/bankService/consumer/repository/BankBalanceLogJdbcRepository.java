package com.rabbitmq.bankService.consumer.repository;


import com.rabbitmq.bankService.consumer.entity.BankBalanceLogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BankBalanceLogJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL =
            "INSERT INTO bank_balance_log (prev_data, current_data, class_method) " +
            "VALUES (?, ?, ?)";
    public void saveAll(List<BankBalanceLogEntity> logs)throws SQLException {

        jdbcTemplate.batchUpdate(SQL, logs, logs.size(), (ps, log) -> {
            ps.setString(1, log.getPrev_data());
            ps.setString(2, log.getCurrent_data());
            ps.setString(3, log.getClass_method());
        });
    }

}

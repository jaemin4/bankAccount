package com.rabbitmq.bankService.consumer.repository;

import com.rabbitmq.bankService.consumer.entity.AccessLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogEntityRepository extends JpaRepository<AccessLogEntity, Long> {
}
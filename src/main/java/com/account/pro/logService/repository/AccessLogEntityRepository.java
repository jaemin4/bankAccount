package com.account.pro.logService.repository;

import com.account.pro.logService.repository.entity.AccessLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogEntityRepository extends JpaRepository<AccessLogEntity, Long> {
}
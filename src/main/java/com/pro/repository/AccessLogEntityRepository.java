package com.pro.repository;

import com.pro.model.entity.AccessLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogEntityRepository extends JpaRepository<AccessLogEntity, Long> {
}
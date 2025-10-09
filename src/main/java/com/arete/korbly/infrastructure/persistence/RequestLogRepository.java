package com.arete.korbly.infrastructure.persistence;

import com.arete.korbly.infrastructure.domain.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequestLogRepository extends JpaRepository<RequestLog, UUID> {
}

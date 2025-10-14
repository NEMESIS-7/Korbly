package com.arete.korbly.infrastructure.persistence;

import com.arete.korbly.infrastructure.domain.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, UUID> {
}

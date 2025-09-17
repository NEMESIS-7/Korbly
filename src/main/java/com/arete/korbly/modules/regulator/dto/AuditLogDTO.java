package com.arete.korbly.modules.regulator.dto;

import com.arete.korbly.modules.regulator.enums.ActionType;
import com.arete.korbly.modules.shared.enums.UserType;

import java.sql.Timestamp;
import java.util.UUID;

public record AuditLogDTO(
        UUID logId,
        UUID actorId,
        UserType actorRole,
        String action,
        String ipAddress,
        String requestId,
        UUID entityId,
        ActionType actionType,
        String entityType,
        String details,
        Timestamp timestamp,
        Timestamp createdOn) {
}

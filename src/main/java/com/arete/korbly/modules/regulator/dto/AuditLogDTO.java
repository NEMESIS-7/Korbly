package com.arete.korbly.modules.regulator.dto;

import com.arete.korbly.modules.regulator.enums.ActionType;
import com.arete.korbly.modules.regulator.enums.EntityType;
import com.arete.korbly.modules.shared.enums.UserType;

import java.sql.Timestamp;
import java.util.UUID;

public record AuditLogDTO(
        UUID logId,
        UUID actorId,
        UserType actorType,
        ActionType actionType,
        EntityType entityType,
        String details,
        Timestamp timestamp
) {
}

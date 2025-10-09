package com.arete.korbly.modules.termsheet.dto;

import com.arete.korbly.modules.termsheet.enums.CPStatus;
import com.arete.korbly.modules.shared.enums.DeleteYn;

import java.sql.Timestamp;
import java.util.UUID;

public record ConditionPrecedentDTO(
        UUID cpId,
        UUID sheetId,
        String title,
        String description,
        Boolean required,
        CPStatus status,
        String evidenceFileKey,
        String note,
        UUID approvedBy,
        String waiverReason,
        DeleteYn deleteYn,
        Timestamp createdAt,
        Timestamp updatedAt
) {}
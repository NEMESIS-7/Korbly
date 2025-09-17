package com.arete.korbly.modules.regulator.dto;

import com.arete.korbly.modules.regulator.enums.RegulatorStatus;
import com.arete.korbly.modules.regulator.enums.RegulatorType;

import java.sql.Timestamp;
import java.util.UUID;

public record RegulatorDTO(
        UUID regulatorId,
        String regulatorName,
        String regulatorJurisdiction,
        RegulatorType regulatorType,
        RegulatorStatus regulatorStatus,
        Timestamp createdAt,
        Timestamp updatedAt
) {
}

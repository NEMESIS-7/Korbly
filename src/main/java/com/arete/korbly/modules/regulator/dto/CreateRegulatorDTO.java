package com.arete.korbly.modules.regulator.dto;

import com.arete.korbly.modules.regulator.enums.RegulatorStatus;
import com.arete.korbly.modules.regulator.enums.RegulatorType;

import java.util.UUID;

public record CreateRegulatorDTO(
        String regulatorName,
        String regulatorJurisdiction,
        String regulatorEmail,
        String regulatorPhone,
        RegulatorStatus regulatorStatus,
        RegulatorType regulatorType,
        UUID userId
) {
}

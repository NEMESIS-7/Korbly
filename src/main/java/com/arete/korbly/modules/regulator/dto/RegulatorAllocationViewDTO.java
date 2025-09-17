package com.arete.korbly.modules.regulator.dto;

import com.arete.korbly.modules.shared.enums.InvestorType;
import com.arete.korbly.modules.syndication.enums.AllocationStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public record RegulatorAllocationViewDTO(
        UUID allocationId,
        UUID investorId,
        InvestorType investorType,
        BigDecimal amount,
        AllocationStatus allocationStatus,
        UUID confirmedBy,
        Timestamp confirmedAt
) {
}

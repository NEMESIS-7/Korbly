package com.arete.korbly.modules.syndication.dto;

import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.syndication.enums.AllocationStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public record AllocationDTO(
        UUID trancheId,
        UUID investorId,
        BigDecimal amount,
        AllocationStatus allocationStatus,
        Timestamp createdAt,
        Timestamp updatedAt,
        UUID confirmedBy,
        DeleteYn deleteYn,
        UUID allocationId) {
}

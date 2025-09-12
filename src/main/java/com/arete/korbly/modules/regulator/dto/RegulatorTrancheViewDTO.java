package com.arete.korbly.modules.regulator.dto;

import com.arete.korbly.modules.syndication.enums.TrancheType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record RegulatorTrancheViewDTO(
        UUID trancheId,
        TrancheType trancheType,
        BigDecimal trancheAmount,
        BigDecimal allocatedSoFar,
        BigDecimal remainingCapacity,
        BigDecimal interestRate,
        Integer tenorMonths,
        List<RegulatorAllocationViewDTO> allocations
) {
}

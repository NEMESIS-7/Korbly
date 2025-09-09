package com.arete.korbly.modules.syndication.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record InvestorTrancheViewDTO(
        UUID trancheId,
        String trancheType,   // SENIOR, MEZZANINE, JUNIOR
        BigDecimal amount,    // total tranche size
        BigDecimal interestRate,
        Integer tenorMonths,
        BigDecimal remainingCapacity // amount not yet allocated
) {}

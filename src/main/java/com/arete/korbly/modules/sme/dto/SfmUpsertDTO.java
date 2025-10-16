package com.arete.korbly.modules.sme.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SfmUpsertDTO(
        LocalDate month,
        BigDecimal revenue,
        BigDecimal operatingCashflow
) {}
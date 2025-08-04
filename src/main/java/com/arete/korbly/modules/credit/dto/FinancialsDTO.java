package com.arete.korbly.modules.credit.dto;

import java.math.BigDecimal;

public record FinancialsDTO(
        BigDecimal totalAssets,
        BigDecimal totalLiabilities,
        BigDecimal currentAssets,
        BigDecimal currentLiabilities,
        BigDecimal workingCapital,
        BigDecimal retainedEarnings,
        BigDecimal ebit, // earnings before interest and tax
        BigDecimal sales,
        BigDecimal marketValueEquity,
        BigDecimal totalDebt,
        BigDecimal cashFlow,
        BigDecimal netIncome,
        BigDecimal companySize
) {}
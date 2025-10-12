package com.arete.korbly.modules.valuation.dto;

import com.arete.korbly.modules.termsheet.enums.AmortizationStructure;

import java.math.BigDecimal;

public record ValuationOverrideDTO(
        BigDecimal principal,
        Double rateAnnual,
        Integer tenorMonths,
        AmortizationStructure amortization,
        Integer gracePrincipalMonths,
        Integer graceInterestMonths,
        Double feeUpfrontPct,
        Double feeServicingBps,
        Double feeExitPct,
        Double discountRateAnnual,
        String currency
) {}

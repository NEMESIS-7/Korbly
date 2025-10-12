package com.arete.korbly.modules.valuation.dto;

import java.math.BigDecimal;
import java.util.List;

public record ValuationPreviewResponse(
        BigDecimal netPresentValue,
        Double internalRateOfReturnAnnual,
        Double cashOnCashMultiple,
        Integer paybackPeriodInMonths,
        List<CashflowRow> cashflowSchedule,
        List<double[]> npvVsDiscountRate,
        List<double[]> npvVsTenorMonths,
        String warning
) {}

package com.arete.korbly.modules.valuation.dto;

import com.arete.korbly.modules.syndication.enums.DealCurrency;
import com.arete.korbly.modules.termsheet.enums.AmortizationStructure;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ValuationPreviewRequest(
        UUID dealId,
        UUID trancheId,
        UUID termSheetId,

        BigDecimal principalAmount,
        BigDecimal annualInterestRate,
        int totalTenorInMonths,
        AmortizationStructure amortizationStructure,

        int graceMonthsForPrincipal,
        int graceMonthsForInterest,

        BigDecimal upfrontFeePercent,
        BigDecimal servicingFeeBasisPoints,
        BigDecimal exitFeePercent,

        BigDecimal balloonPercentOfOriginal,
        BigDecimal balloonAmountAtMaturity,
        BigDecimal fixedMonthlyPayment,

        Integer negativeAmortizationMonths,
        BigDecimal minPaymentPercentOfInterest,
        BigDecimal minPaymentAbsoluteAmount,
        BigDecimal negativeAmortizationCapMultiple,

        LocalDate scheduleStartDate,
        DealCurrency currencyCode,

        BigDecimal investorDiscountRateAnnual,
        String scenarioLabel
) {
}

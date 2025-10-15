package com.arete.korbly.modules.investor.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class PortfolioSummaryDTO {

    private final UUID investorId;
    private final LocalDateTime asOf;
    private final BigDecimal portfolioValue;
    private final int activeInvestments;
    private final BigDecimal totalAmount;
    private final BigDecimal weightedIrrSum;
    private final BigDecimal weightedNpvSum;
    private final BigDecimal weightedRiskSum;
    private final Double riskScore;
    private final BigDecimal annualAverageYield;
    private final String valuationMethod;
    private final String yieldMethod;
    private final String riskMethod;

    public PortfolioSummaryDTO(
            UUID investorId,
            Timestamp asOf,
            BigDecimal portfolioValue,
            int activeInvestments,
            BigDecimal totalAmount,
            BigDecimal weightedIrrSum,
            BigDecimal weightedNpvSum,
            BigDecimal weightedRiskSum,
            String valuationMethod,
            String yieldMethod,
            String riskMethod
    ) {
        this.investorId = investorId;
        this.asOf = asOf != null ? asOf.toLocalDateTime() : LocalDateTime.now();
        this.portfolioValue = portfolioValue;
        this.activeInvestments = activeInvestments;
        this.totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO;
        this.weightedIrrSum = weightedIrrSum != null ? weightedIrrSum : BigDecimal.ZERO;
        this.weightedNpvSum = weightedNpvSum != null ? weightedNpvSum : BigDecimal.ZERO;
        this.weightedRiskSum = weightedRiskSum != null ? weightedRiskSum : BigDecimal.ZERO;
        this.valuationMethod = valuationMethod;
        this.yieldMethod = yieldMethod;
        this.riskMethod = riskMethod;

        if (this.totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            this.annualAverageYield = BigDecimal.ZERO;
            this.riskScore = 0.0;
        } else {
            this.annualAverageYield = weightedIrrSum != null ? weightedIrrSum.divide(this.totalAmount, 6, RoundingMode.HALF_UP) : null;
            this.riskScore = weightedRiskSum != null ? weightedRiskSum.divide(this.totalAmount, 6, RoundingMode.HALF_UP).doubleValue() : null;
        }
    }
}
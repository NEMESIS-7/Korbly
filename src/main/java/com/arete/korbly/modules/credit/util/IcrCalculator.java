package com.arete.korbly.modules.credit.util;

import com.arete.korbly.modules.credit.dto.FinancialsDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculates Interest Coverage Ratio (ICR)
 * ICR = EBIT / Interest Expense
 *
 * Measures how many times a company can cover its interest payments with earnings.
 * - ICR > 2.5: Strong ability to meet interest obligations
 * - ICR 1.5-2.5: Adequate coverage
 * - ICR < 1.5: Limited ability to cover interest
 * - ICR < 1.0: Unable to cover interest from operating earnings
 */
@Component
public class IcrCalculator {

    /**
     * Calculates ICR using EBIT and interest expense
     *
     * @param dto Financial data
     * @param interestExpense Annual interest expense
     * @return ICR ratio
     */
    public BigDecimal calculate(FinancialsDTO dto, BigDecimal interestExpense) {
        if (interestExpense == null || interestExpense.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal ebit = dto.ebit();
        if (ebit == null || ebit.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return ebit.divide(interestExpense, 4, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Alternative calculation using EBITDA (more conservative)
     * Requires depreciation and amortization to be added to EBIT
     *
     * @param dto Financial data
     * @param interestExpense Annual interest expense
     * @param depreciationAndAmortization D&A amount
     * @return EBITDA-based ICR ratio
     */
    public BigDecimal calculateWithEbitda(FinancialsDTO dto, BigDecimal interestExpense,
                                          BigDecimal depreciationAndAmortization) {
        if (interestExpense == null || interestExpense.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal ebit = dto.ebit();
        if (ebit == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal ebitda = ebit.add(depreciationAndAmortization != null ?
                depreciationAndAmortization : BigDecimal.ZERO);

        if (ebitda.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return ebitda.divide(interestExpense, 4, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
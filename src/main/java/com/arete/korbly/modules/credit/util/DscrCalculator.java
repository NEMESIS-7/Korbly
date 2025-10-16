package com.arete.korbly.modules.credit.util;

import com.arete.korbly.modules.credit.dto.FinancialsDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculates Debt Service Coverage Ratio (DSCR)
 * DSCR = Net Operating Income / Total Debt Service
 *
 * A ratio above 1.0 indicates sufficient income to cover debt obligations.
 * - DSCR > 1.25: Strong ability to service debt
 * - DSCR 1.0-1.25: Adequate coverage
 * - DSCR < 1.0: Insufficient income to cover debt payments
 */
@Component
public class DscrCalculator {

    /**
     * Calculates DSCR using cash flow as proxy for net operating income
     * and assuming total debt service equals total debt (simplified version)
     *
     * For more accurate calculation, pass debt service payments separately
     */
    public BigDecimal calculate(FinancialsDTO dto) {
        return calculate(dto, dto.totalDebt());
    }

    /**
     * Calculates DSCR with explicit debt service amount
     *
     * @param dto Financial data
     * @param debtService Annual debt service (principal + interest payments)
     * @return DSCR ratio
     */
    public BigDecimal calculate(FinancialsDTO dto, BigDecimal debtService) {
        if (debtService == null || debtService.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal netOperatingIncome = dto.cashFlow();
        if (netOperatingIncome == null) {
            // Fallback to EBIT if cash flow not available
            netOperatingIncome = dto.ebit();
        }

        if (netOperatingIncome == null || netOperatingIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return netOperatingIncome.divide(debtService, 4, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
    }
}

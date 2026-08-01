package com.arete.korbly.modules.credit.util;

import com.arete.korbly.modules.credit.dto.FinancialsDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class OhlsonScoreCalculator {

    public BigDecimal calculate(FinancialsDTO dto) {
        BigDecimal totalAssets = dto.totalAssets();
        BigDecimal totalLiabilities = dto.totalLiabilities();
        if (totalAssets == null || totalAssets.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        double ta = totalAssets.doubleValue();
        double tl = totalLiabilities != null ? totalLiabilities.doubleValue() : 0.0;
        double wc = dto.workingCapital() != null ? dto.workingCapital().doubleValue() : 0.0;
        double ca = dto.currentAssets() != null ? dto.currentAssets().doubleValue() : 0.0;
        double cl = dto.currentLiabilities() != null ? dto.currentLiabilities().doubleValue() : 0.0;
        double ni = dto.netIncome() != null ? dto.netIncome().doubleValue() : 0.0;
        double ffo = dto.cashFlow() != null ? dto.cashFlow().doubleValue() : 0.0;

        int negNetIncome = ni < 0 ? 1 : 0;
        int tlGtTa = tl > ta ? 1 : 0;

        double ffoOverTl = tl != 0.0 ? ffo / tl : 0.0;
        double clOverCa = ca != 0.0 ? cl / ca : 0.0;

        double o = -1.32
                - 0.407 * Math.log(ta)
                + 6.03 * (tl / ta)
                - 1.43 * (wc / ta)
                + 0.0757 * clOverCa
                - 2.37 * (ni / ta)
                - 1.83 * ffoOverTl
                + 0.285 * negNetIncome
                - 0.521 * tlGtTa;

        double probability = 1 / (1 + Math.exp(-o));
        return BigDecimal.valueOf(probability).setScale(4, RoundingMode.HALF_UP);
    }
}

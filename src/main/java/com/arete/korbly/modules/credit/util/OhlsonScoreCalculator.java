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
        double tl = totalLiabilities.doubleValue();
        double wc = dto.workingCapital().doubleValue();
        double ca = dto.currentAssets().doubleValue();
        double cl = dto.currentLiabilities().doubleValue();
        double ni = dto.netIncome().doubleValue();
        double ffo = dto.cashFlow().doubleValue();

        int negNetIncome = ni < 0 ? 1 : 0;
        int tlGtTa = tl > ta ? 1 : 0;

        double o = -1.32
                - 0.407 * Math.log(ta)
                + 6.03 * (tl / ta)
                - 1.43 * (wc / ta)
                + 0.0757 * (cl / ca)
                - 2.37 * (ni / ta)
                - 1.83 * (ffo / tl)
                + 0.285 * negNetIncome
                - 0.521 * tlGtTa;

        double probability = 1 / (1 + Math.exp(-o));
        return BigDecimal.valueOf(probability).setScale(4, RoundingMode.HALF_UP);
    }
}

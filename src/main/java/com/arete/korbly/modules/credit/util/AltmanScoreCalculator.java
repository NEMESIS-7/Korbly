package com.arete.korbly.modules.credit.util;

import com.arete.korbly.modules.credit.dto.FinancialsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Slf4j
public class AltmanScoreCalculator {

    public BigDecimal calculate(FinancialsDTO dto) {
        log.debug("computing Altman Z-score for financials");
        BigDecimal totalAssets = dto.totalAssets();
        if (totalAssets == null || totalAssets.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal workingCapital = dto.workingCapital() != null ? dto.workingCapital() : BigDecimal.ZERO;
        BigDecimal retainedEarnings = dto.retainedEarnings() != null ? dto.retainedEarnings() : BigDecimal.ZERO;
        BigDecimal ebit = dto.ebit() != null ? dto.ebit() : BigDecimal.ZERO;
        BigDecimal totalLiabilities = dto.totalLiabilities();

        BigDecimal z = BigDecimal.ZERO;

        z = z.add(workingCapital.divide(totalAssets, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(6.56)));
        z = z.add(retainedEarnings.divide(totalAssets, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(3.26)));
        z = z.add(ebit.divide(totalAssets, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(6.72)));

        if (totalLiabilities != null && totalLiabilities.compareTo(BigDecimal.ZERO) != 0) {
            z = z.add(
                    totalAssets.subtract(totalLiabilities)
                            .divide(totalLiabilities, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(1.05))
            );
        }

        return z.setScale(2, RoundingMode.HALF_UP);
    }
}

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
        System.out.println("financials: " + dto);
        BigDecimal totalAssets = dto.totalAssets();
        if (totalAssets == null || totalAssets.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal z = BigDecimal.ZERO;


        z = z.add(
                dto.workingCapital().divide(totalAssets, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(6.56))
        );
        z = z.add(
                dto.retainedEarnings().divide(totalAssets, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(3.26))
        );
        z = z.add(
                dto.ebit().divide(totalAssets, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(6.72))
        );
        z = z.add(
                dto.totalAssets().subtract(dto.totalLiabilities()).divide(dto.totalLiabilities(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(1.05))
        );

        return z.setScale(2, RoundingMode.HALF_UP);
    }
}

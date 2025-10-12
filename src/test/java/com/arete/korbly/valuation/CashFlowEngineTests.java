package com.arete.korbly.valuation;

import com.arete.korbly.modules.termsheet.enums.AmortizationStructure;
import com.arete.korbly.modules.valuation.domain.CashFlowAssumption;
import com.arete.korbly.modules.valuation.dto.CashflowRow;
import com.arete.korbly.modules.valuation.service.CashFlowEngine;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CashFlowEngineTests {
    @Test
    void bulletBasic() {
        CashFlowAssumption a = new CashFlowAssumption();
        a.principal = new BigDecimal("100000");
        a.rateAnnual = 0.12;
        a.tenorMonths = 12;
        a.amortization = AmortizationStructure.BULLET_AMORTIZATION;
        a.feeUpfrontPct = 0.01; // 1%
        a.feeServicingBps = 0.0;
        a.feeExitPct = 0.02; // 2% at maturity

        CashFlowEngine engine = new CashFlowEngine();
        List<CashflowRow> rows = engine.buildSchedule(a);

        assertEquals(13, rows.size()); // t=0 + 12 months
        CashflowRow t0 = rows.getFirst();
        assertEquals(0, t0.total().compareTo(new BigDecimal("-99000.00"))); // -100k + 1k upfront = -99k

        CashflowRow last = rows.get(12);
        // last month's total ≈ interest + principal + exit
        // interest ~ 100k * 0.12 / 12 = 1000.00
        // principal = 100k
        // exit = 2% * 100k = 2k
        assertEquals(new BigDecimal("103000.00"), last.total());
        assertEquals(new BigDecimal("0.00"), last.closing());
    }
}

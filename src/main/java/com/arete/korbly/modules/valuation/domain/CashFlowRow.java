package com.arete.korbly.modules.valuation.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CashFlowRow {
    public final int monthIndex;       // 0..tenor
    public final LocalDate date;       // month end
    public final BigDecimal opening;   // principal at start of period
    public final BigDecimal interest;  // gross interest for the month
    public final BigDecimal principal; // principal repaid this month
    public final BigDecimal feesNet;   // servicing (-) monthly; upfront (+) t=0; exit (+) at maturity
    public final BigDecimal total;     // investor CF this month (interest - servicing + principal + exit) + (t=0 includes -principal + upfront)
    public final BigDecimal closing;   // principal at end of period

    public CashFlowRow(int monthIndex, LocalDate date, BigDecimal opening, BigDecimal interest, BigDecimal principal, BigDecimal feesNet, BigDecimal total, BigDecimal closing) {
        this.monthIndex = monthIndex;
        this.date = date;
        this.opening = opening;
        this.interest = interest;
        this.principal = principal;
        this.feesNet = feesNet;
        this.total = total;
        this.closing = closing;
    }
}

package com.arete.korbly.modules.valuation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CashFlowLineItem {
    private int periodIndex;
    private LocalDate periodEndDate;
    private BigDecimal openingBalance;
    private BigDecimal interestAccrued;
    private BigDecimal principalRepaid;
    private BigDecimal feesNetForPeriod;
    private BigDecimal totalCashflowForInvestor;
    private BigDecimal closingBalance;

    public int getPeriodIndex() {
        return periodIndex;
    }

    public void setPeriodIndex(int periodIndex) {
        this.periodIndex = periodIndex;
    }

    public LocalDate getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(LocalDate periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public BigDecimal getInterestAccrued() {
        return interestAccrued;
    }

    public void setInterestAccrued(BigDecimal interestAccrued) {
        this.interestAccrued = interestAccrued;
    }

    public BigDecimal getPrincipalRepaid() {
        return principalRepaid;
    }

    public void setPrincipalRepaid(BigDecimal principalRepaid) {
        this.principalRepaid = principalRepaid;
    }

    public BigDecimal getFeesNetForPeriod() {
        return feesNetForPeriod;
    }

    public void setFeesNetForPeriod(BigDecimal feesNetForPeriod) {
        this.feesNetForPeriod = feesNetForPeriod;
    }

    public BigDecimal getTotalCashflowForInvestor() {
        return totalCashflowForInvestor;
    }

    public void setTotalCashflowForInvestor(BigDecimal totalCashflowForInvestor) {
        this.totalCashflowForInvestor = totalCashflowForInvestor;
    }

    public BigDecimal getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(BigDecimal closingBalance) {
        this.closingBalance = closingBalance;
    }
}

package com.arete.korbly.modules.valuation.domain;

import com.arete.korbly.modules.syndication.enums.DealCurrency;
import com.arete.korbly.modules.termsheet.enums.AmortizationStructure;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CashFlowAssumption {
    public BigDecimal principal;             // loan amount (e.g., 1_000_000.00)
    public double rateAnnual;                // 0.18 = 18% p.a.
    public int tenorMonths;                  // number of months
    public AmortizationStructure amortization; // FULL_AMORTIZATION, PARTIAL_AMORTIZATION, ...

    // Grace (optional)
    public int gracePrincipalMonths = 0;     // months with 0 principal
    public int graceInterestMonths = 0;      // months with 0 interest

    // Fees
    public double feeUpfrontPct = 0.0;       // e.g., 0.01 = 1% of principal, paid at t=0 to investor
    public double feeServicingBps = 0.0;     // e.g., 50 bps/year => 0.50% p.a., taken monthly from interest
    public double feeExitPct = 0.0;          // e.g., 0.02 = 2% on outstanding at maturity (we’ll use opening of final month)

    // PARTIAL_AMORTIZATION knobs (pick one: balloonPct or balloonAmount; optional)
    public Double balloonPct;                // 0.30 = 30% of original principal as balloon
    public BigDecimal balloonAmount;         // explicit balloon amount
    public BigDecimal fixedMonthlyPayment;   // alternative mode (must be >= monthly interest)

    // NEGATIVE_AMORTIZATION knobs (v1 minimal)
    public Integer negAmMonths;              // how many months we allow neg-am (default = tenor)
    public Double minPaymentPctOfInterest;   // e.g., 0.50 = pay 50% of interest
    public BigDecimal minPaymentAmount;      // alternative to pct
    public Double negAmCapPct;               // e.g., 1.25 = 125% of original principal
    public String negAmEndMode = "BALLOON";  // v1: BALLOON

    // Misc
    public LocalDate startDate = LocalDate.now();
    public DealCurrency currency = DealCurrency.GHS;

    public CashFlowAssumption() {
    }

    public CashFlowAssumption(BigDecimal principal, double rateAnnual, int tenorMonths, AmortizationStructure amortization, int gracePrincipalMonths, int graceInterestMonths, double feeUpfrontPct, double feeServicingBps, double feeExitPct, Double balloonPct, BigDecimal balloonAmount, BigDecimal fixedMonthlyPayment, Integer negAmMonths, Double minPaymentPctOfInterest, BigDecimal minPaymentAmount, Double negAmCapPct, String negAmEndMode, LocalDate startDate, DealCurrency currency) {
        this.principal = principal;
        this.rateAnnual = rateAnnual;
        this.tenorMonths = tenorMonths;
        this.amortization = amortization;
        this.gracePrincipalMonths = gracePrincipalMonths;
        this.graceInterestMonths = graceInterestMonths;
        this.feeUpfrontPct = feeUpfrontPct;
        this.feeServicingBps = feeServicingBps;
        this.feeExitPct = feeExitPct;
        this.balloonPct = balloonPct;
        this.balloonAmount = balloonAmount;
        this.fixedMonthlyPayment = fixedMonthlyPayment;
        this.negAmMonths = negAmMonths;
        this.minPaymentPctOfInterest = minPaymentPctOfInterest;
        this.minPaymentAmount = minPaymentAmount;
        this.negAmCapPct = negAmCapPct;
        this.negAmEndMode = negAmEndMode;
        this.startDate = startDate;
        this.currency = currency;
    }
}

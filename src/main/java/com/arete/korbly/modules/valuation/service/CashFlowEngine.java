package com.arete.korbly.modules.valuation.service;

import com.arete.korbly.modules.termsheet.enums.AmortizationStructure;
import com.arete.korbly.modules.valuation.domain.CashFlowAssumption;
import com.arete.korbly.modules.valuation.dto.CashflowRow;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CashFlowEngine implements ICashflowEngine {

    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final MathContext MC = MathContext.DECIMAL64;
    private static final int SCALE = 2; // currency scale

    @Override
    public List<CashflowRow> buildSchedule(CashFlowAssumption a) {
        validate(a);

        List<CashflowRow> rows = new ArrayList<>();
        BigDecimal P0 = a.principal.setScale(SCALE, RoundingMode.HALF_UP);
        BigDecimal opening = P0;

        double r_m = a.rateAnnual / 12.0;                         // monthly interest rate (decimal)
        double servicingMonthly = (a.feeServicingBps / 10000.0) / 12.0; // bps -> decimal per month

        // t = 0 row (funding and upfront fee) ---
        BigDecimal upfront = P0.multiply(bd(a.feeUpfrontPct));
        // investor outflow: -principal ; investor receives upfront (+)
        BigDecimal t0Total = P0.negate().add(upfront).setScale(SCALE, RoundingMode.HALF_UP);
        rows.add(new CashflowRow(
                0, a.startDate, opening,
                ZERO, ZERO,
                upfront.setScale(SCALE, RoundingMode.HALF_UP),
                t0Total,
                opening
        ));

        // Precompute annuity payment if needed (after grace)
        BigDecimal annuityA = null;
        if (a.amortization == AmortizationStructure.FULL_AMORTIZATION) {
            int nEff = Math.max(0, a.tenorMonths - a.gracePrincipalMonths);
            annuityA = (nEff > 0) ? annuityPayment(P0, r_m, nEff) : ZERO;
        }

        // Precompute partial amort fixed payment if balloon mode
        BigDecimal partialPaymentA = null;
        if (a.amortization == AmortizationStructure.PARTIAL_AMORTIZATION) {
            if (a.balloonAmount != null || a.balloonPct != null) {
                BigDecimal balloon = (a.balloonAmount != null)
                        ? a.balloonAmount
                        : P0.multiply(bd(a.balloonPct));
                int nEff = Math.max(0, a.tenorMonths - a.gracePrincipalMonths);
                partialPaymentA = paymentToReachBalloon(P0, r_m, nEff, balloon);
            }
        }

        // --- months 1..tenor ---
        for (int t = 1; t <= a.tenorMonths; t++) {
            LocalDate date = a.startDate.plusMonths(t);

            // gross interest
            BigDecimal interest = (t <= a.graceInterestMonths)
                    ? ZERO
                    : opening.multiply(bd(r_m), MC);

            // servicing (reduces investor's net interest)
            BigDecimal servicing = opening.multiply(bd(servicingMonthly), MC);

            // principal by amortization type
            BigDecimal principal = switch (a.amortization) {
                case FULL_AMORTIZATION -> calcFullPrincipal(a, t, interest, annuityA);
                case STRAIGHT_LINE_AMORTIZATION -> calcStraightLinePrincipal(a, t);
                case BULLET_AMORTIZATION -> (t == a.tenorMonths) ? opening : ZERO;
                case PARTIAL_AMORTIZATION -> calcPartialPrincipal(a, t, interest, partialPaymentA);
                case NEGATIVE_AMORTIZATION -> ZERO; // principal usually 0 during neg-am; handled via capitalization below
            };

            BigDecimal closing;

            // Negative amortization special handling (capitalize shortfall)
            if (a.amortization == AmortizationStructure.NEGATIVE_AMORTIZATION) {
                BigDecimal minPay = computeNegAmMinPay(a, interest);
                BigDecimal shortfall = interest.subtract(minPay).max(ZERO);
                closing = opening.add(shortfall).setScale(SCALE, RoundingMode.HALF_UP);

                // Cap the growth if a cap is set
                if (a.negAmCapPct != null && a.negAmCapPct > 1.0) {
                    BigDecimal cap = P0.multiply(bd(a.negAmCapPct));
                    if (closing.compareTo(cap) > 0) closing = cap.setScale(SCALE, RoundingMode.HALF_UP);
                }
            } else {
                closing = opening.subtract(principal).setScale(SCALE, RoundingMode.HALF_UP);
            }

            // Exit fee at maturity: compute on outstanding BEFORE final repayment => use opening
            BigDecimal exit = (t == a.tenorMonths)
                    ? opening.multiply(bd(a.feeExitPct))
                    : ZERO;

            // Net interest after servicing
            BigDecimal netInterest = interest.subtract(servicing);

            // Fees column (for visibility): monthly (-servicing), last month adds +exit
            BigDecimal feesNet = exit.subtract(servicing).setScale(SCALE, RoundingMode.HALF_UP);

            // Total investor CF this month (ex t=0 already handled)
            BigDecimal total = netInterest.add(principal).add(exit)
                    .setScale(SCALE, RoundingMode.HALF_UP);

            rows.add(new CashflowRow(
                    t, date,
                    opening.setScale(SCALE, RoundingMode.HALF_UP),
                    interest.setScale(SCALE, RoundingMode.HALF_UP),
                    principal.setScale(SCALE, RoundingMode.HALF_UP),
                    feesNet,
                    total,
                    closing
            ));

            opening = closing;
        }

        // ensure last closing is 0 for non-neg-am types
        rows = reconcileLastRowIfNeeded(rows, a);

        return rows;
    }


    private BigDecimal calcFullPrincipal(CashFlowAssumption a, int t, BigDecimal interest, BigDecimal annuityA) {
        if (t <= a.gracePrincipalMonths) {
            return ZERO;
        }
        if (annuityA == null || annuityA.signum() == 0){
            return ZERO;
        }
        BigDecimal p = annuityA.subtract(interest);
        return p.max(ZERO);
    }

    private BigDecimal calcStraightLinePrincipal(CashFlowAssumption a, int t) {
        if (t <= a.gracePrincipalMonths){
            return ZERO;
        }
        int nEff = Math.max(1, a.tenorMonths - a.gracePrincipalMonths);
        return a.principal.divide(BigDecimal.valueOf(nEff), MC);
    }

    private BigDecimal calcPartialPrincipal(CashFlowAssumption a, int t, BigDecimal interest, BigDecimal partialPaymentA) {
        if (t <= a.gracePrincipalMonths){
            return ZERO;
        }

        // Mode 1: balloon specified => use computed "annuity to balloon"
        if (partialPaymentA != null) {
            return partialPaymentA.subtract(interest).max(ZERO);
        }
        // Mode 2: fixed monthly payment given
        if (a.fixedMonthlyPayment != null) {
            // IMPORTANT: must be >= interest to avoid negative amortization in PARTIAL mode
            if (a.fixedMonthlyPayment.compareTo(interest) < 0) {
                throw new IllegalArgumentException("Fixed monthly payment < interest; this would be NEGATIVE_AMORTIZATION. Rejecting in PARTIAL mode.");
            }
            return a.fixedMonthlyPayment.subtract(interest).max(ZERO);
        }
        throw new IllegalArgumentException("PARTIAL_AMORTIZATION requires balloonPct/Amount or fixedMonthlyPayment.");
    }

    private BigDecimal annuityPayment(BigDecimal P0, double r_m, int n) {
        if (n <= 0) {
            return ZERO;
        }
        if (r_m == 0.0) { // no interest: straight division
            return P0.divide(BigDecimal.valueOf(n), MC);
        }
        double pow = Math.pow(1 + r_m, -n);
        double A = P0.doubleValue() * (r_m / (1 - pow));
        return bd(A);
    }

    // A to reach a target balloon after n periods at rate r_m
    private BigDecimal paymentToReachBalloon(BigDecimal P0, double r_m, int n, BigDecimal balloon) {
        if (n <= 0) {
            return ZERO;
        }
        if (r_m == 0.0) {
            // linear: A = (P0 - balloon)/n
            return P0.subtract(balloon).divide(BigDecimal.valueOf(n), MC).max(ZERO);
        }
        double onePlusPowN = Math.pow(1 + r_m, n);
        double numer = r_m * (P0.doubleValue() * onePlusPowN - balloon.doubleValue());
        double denom = onePlusPowN - 1.0;
        double A = numer / denom;
        return bd(Math.max(A, 0.0));
    }

    private BigDecimal computeNegAmMinPay(CashFlowAssumption a, BigDecimal interest) {
        if (a.minPaymentAmount != null) {
            return a.minPaymentAmount;
        }
        double pct = (a.minPaymentPctOfInterest != null) ? a.minPaymentPctOfInterest : 0.0;
        return interest.multiply(bd(pct), MC);
    }

    private List<CashflowRow> reconcileLastRowIfNeeded(List<CashflowRow> rows, CashFlowAssumption a) {
        if (rows.isEmpty()) {
            return rows;
        }
        // For NON negative-am types, make sure closing=0 exactly by nudging last principal a few cents
        if (a.amortization == AmortizationStructure.NEGATIVE_AMORTIZATION) {
            return rows;
        }

        CashflowRow last = rows.get(rows.size() - 1);
        if (last.closing().compareTo(ZERO.setScale(SCALE)) == 0) {
            return rows;
        }

        BigDecimal delta = last.closing(); // amount we need to reduce
        CashflowRow fixed = new CashflowRow(
                last.monthIndex(),
                last.date(),
                last.opening(),
                last.interest(),
                last.principal().add(delta).setScale(SCALE, RoundingMode.HALF_UP),
                last.feesNet(),
                last.total().add(delta).setScale(SCALE, RoundingMode.HALF_UP),
                ZERO.setScale(SCALE, RoundingMode.HALF_UP)
        );
        rows.set(rows.size() - 1, fixed);
        return rows;
    }

    private void validate(CashFlowAssumption a) {
        if (a.principal == null || a.principal.signum() <= 0){
            throw new IllegalArgumentException("principal must be > 0");
        }
        if (a.tenorMonths < 1){
            throw new IllegalArgumentException("tenorMonths must be >= 1");
        }
        if (a.gracePrincipalMonths < 0 || a.graceInterestMonths < 0) {
            throw new IllegalArgumentException("grace months cannot be negative");
        }
        if (a.gracePrincipalMonths > a.tenorMonths || a.graceInterestMonths > a.tenorMonths) {
            throw new IllegalArgumentException("grace months cannot exceed tenor");
        }
        if (a.feeUpfrontPct < 0 || a.feeExitPct < 0 || a.feeServicingBps < 0){
            throw new IllegalArgumentException("fees cannot be negative");
        }

        if (a.amortization == AmortizationStructure.PARTIAL_AMORTIZATION) {
            boolean hasBalloon = (a.balloonAmount != null) || (a.balloonPct != null);
            boolean hasFixedPay = (a.fixedMonthlyPayment != null);
            if (!hasBalloon && !hasFixedPay) {
                throw new IllegalArgumentException("PARTIAL_AMORTIZATION requires balloonPct/Amount or fixedMonthlyPayment.");
            }
            if (a.balloonPct != null && (a.balloonPct <= 0.0 || a.balloonPct >= 1.0)) {
                throw new IllegalArgumentException("balloonPct must be between 0 and 1 (exclusive).");
            }
            if (a.balloonAmount != null && a.balloonAmount.compareTo(a.principal) >= 0) {
                throw new IllegalArgumentException("balloonAmount must be less than principal.");
            }
        }

        if (a.amortization == AmortizationStructure.NEGATIVE_AMORTIZATION) {
            if (a.minPaymentAmount == null && a.minPaymentPctOfInterest == null) {
                throw new IllegalArgumentException("NEGATIVE_AMORTIZATION requires minPaymentAmount or minPaymentPctOfInterest.");
            }
            if (a.negAmCapPct != null && a.negAmCapPct < 1.0) {
                throw new IllegalArgumentException("negAmCapPct must be >= 1.0");
            }
        }
    }

    private static BigDecimal bd(double v) {
        return new BigDecimal(Double.toString(v), MC);
    }

    private static BigDecimal bd(Double v) {
        return (v == null) ? ZERO : bd(v.doubleValue());
    }
}


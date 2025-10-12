package com.arete.korbly.modules.valuation.service;

import com.arete.korbly.modules.valuation.dto.CashflowRow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Pure math utilities for valuation: NPV, IRR, Cash-on-Cash, Payback, Sensitivities.
 * All rates passed in as DECIMALS (e.g., 0.21 = 21%).
 * Discount rate is annual; internally we convert to monthly.
 */
public final class ValuationMath {

    private ValuationMath() {}

    /** Returns monthly cashflows (t=0..n) from schedule totals, as doubles for speed. */
    public static double[] toMonthlyCF(List<CashflowRow> rows) {
        double[] cf = new double[rows.size()];
        for (int i = 0; i < rows.size(); i++) {
            // We use "total" because it's already investor-lens cashflow for that month
            cf[i] = rows.get(i).total().doubleValue();
        }
        return cf;
    }

    /** NPV using an ANNUAL discount rate; internally converts to monthly. */
    public static double npvAnnual(double[] monthlyCf, double annualDiscountRate) {
        double r_m = annualToMonthly(annualDiscountRate);
        return npvMonthly(monthlyCf, r_m);
    }

    /** IRR (annualized) using Newton-Raphson with robust fallbacks. Returns null if not solvable. */
    public static Double irrAnnual(double[] monthlyCf) {
        // Check sign change (necessary for IRR to exist)
        if (!hasSignChange(monthlyCf)){
            return null;
        }

        // Solve a monthly IRR first, then annualize.
        Double rMonthly = solveIrrMonthly(monthlyCf);
        if (rMonthly == null){
            return null;
        }
        // Annualize exactly as inverse of annual->monthly compounding
        double annual = Math.pow(1.0 + rMonthly, 12) - 1.0;
        return annual;
    }

    /** Cash-on-Cash multiple = sum(positive CFs) / absolute(sum(negative CFs)). If denom=0 -> null. */
    public static Double cashOnCash(double[] monthlyCf) {
        double inflows = 0.0, outflows = 0.0;
        for (double c : monthlyCf) {
            if (c >= 0) {
                inflows += c;
            } else {
                outflows += c; // outflows is negative
            }
        }
        if (outflows == 0.0){
            return null; // no investment -> undefined
        }
        return inflows / Math.abs(outflows);
    }

    /** Payback month = first t where cumulative CF >= 0. Returns null if never paid back. */
    public static Integer paybackMonth(double[] monthlyCf) {
        double cum = 0.0;
        for (int t = 0; t < monthlyCf.length; t++) {
            cum += monthlyCf[t];
            if (cum >= 0.0){
                return t; // month index (0..n)
            }
        }
        return null;
    }

    /**
     * Simple NPV sensitivity grid for discount rate.
     * Given +/- basis point steps around a center annual rate.
     * Returns list of [rateAnnual, npv] pairs ordered from low->high.
     */
    public static List<double[]> sensitivityNPVbyDiscount(double[] monthlyCf,
                                                          double centerAnnualRate,
                                                          int[] stepBps) {
        List<double[]> out = new ArrayList<>();
        for (int bps : stepBps) {
            double rAnnual = centerAnnualRate + (bps / 10_000.0); // 100 bps = 0.01
            double npv = npvAnnual(monthlyCf, rAnnual);
            out.add(new double[]{ round4(rAnnual), round2(npv) });
        }
        return out;
    }

    /**
     * Optional: tenor sensitivity — trims or extends the tail by k months (simple model).
     * Positive k extends by repeating last monthly CF; negative k truncates final months.
     */
    public static List<double[]> sensitivityNPVbyTenor(double[] monthlyCf,
                                                       double annualDiscountRate,
                                                       int[] deltaMonths) {
        List<double[]> out = new ArrayList<>();
        for (int dm : deltaMonths) {
            double[] adj = adjustTenor(monthlyCf, dm);
            double npv = npvAnnual(adj, annualDiscountRate);
            out.add(new double[]{ dm, round2(npv) });
        }
        return out;
    }


    /** NPV with monthly discount rate r_m. */
    public static double npvMonthly(double[] monthlyCf, double r_m) {
        double npv = 0.0;
        double df = 1.0;          // discount factor for current term
        double step = 1.0 + r_m;  // per-month compounding
        for (int t = 0; t < monthlyCf.length; t++) {
            if (t == 0) {
                df = 1.0; // t=0 undiscounted
            } else {
                df *= (1.0 / step);
            }
            npv += monthlyCf[t] * df;
        }
        return npv;
    }

    /** Try to solve monthly IRR; Newton first, then bisection on a wide bracket if needed. */
    static Double solveIrrMonthly(double[] cf) {
        // 1) Newton-Raphson around a reasonable starting point
        Double nr = newtonIrr(cf, 0.02 /* 2% monthly ~ 26.8% annual*/, 50, 1e-10);
        if (nr != null) {
            return nr;
        }

        // 2) Wide bisection on [-0.9, 2.0] monthly (i.e., [-90%/m, +200%/m])
        return bisectionIrr(cf, -0.9, 2.0, 200, 1e-10);
    }

    /** Newton method; if derivative degenerates or leaves domain, returns null. */
    static Double newtonIrr(double[] cf, double guess, int maxIter, double tol) {
        double r = guess;
        for (int i = 0; i < maxIter; i++) {
            double f = npvMonthly(cf, r);
            double d = dNpvMonthly(cf, r);
            if (Math.abs(d) < 1e-14) {
                return null;
            }
            double rNext = r - f / d;
            if (!Double.isFinite(rNext) || rNext <= -1.0) {
                return null;
            }
            if (Math.abs(rNext - r) < tol){
                return rNext;
            }
            r = rNext;
        }
        return null;
    }

    /** Derivative of NPV wrt monthly rate r. */
    static double dNpvMonthly(double[] cf, double r) {
        double d = 0.0;
        double df = 1.0;
        double step = 1.0 + r;
        for (int t = 0; t < cf.length; t++) {
            if (t == 0) {
                df = 1.0;
                // derivative term for t=0 is 0
            } else {
                df *= (1.0 / step);
                // derivative of (1/step)^t with respect to r is:  -t / (1+r) * (1/step)^t
                double term = -((double) t) * cf[t] * df / step;
                d += term;
            }
        }
        return d;
    }

    /** Bisection method on [lo, hi] where f(lo) and f(hi) have opposite signs. */
    static Double bisectionIrr(double[] cf, double lo, double hi, int maxIter, double tol) {
        double fLo = npvMonthly(cf, lo);
        double fHi = npvMonthly(cf, hi);

        if (Double.isNaN(fLo) || Double.isNaN(fHi)) {
            return null;
        }
        if (fLo == 0.0){
            return lo;
        }
        if (fHi == 0.0) {
            return hi;
        }
        if (fLo * fHi > 0.0){
            return null; // no bracket (no guaranteed root)
        }

        for (int i = 0; i < maxIter; i++) {
            double mid = 0.5 * (lo + hi);
            double fMid = npvMonthly(cf, mid);
            if (Math.abs(fMid) < tol){
                return mid;
            }
            if (fLo * fMid < 0.0) {
                hi = mid; fHi = fMid;
            } else {
                lo = mid; fLo = fMid;
            }
            if (Math.abs(hi - lo) < tol){
                return 0.5 * (lo + hi);
            }
        }
        return 0.5 * (lo + hi);
    }


    /** True if cashflows contain at least one positive and one negative value. */
    static boolean hasSignChange(double[] cf) {
        boolean hasPos = false, hasNeg = false;
        for (double v : cf) {
            if (v > 0){
                hasPos = true;
            }
            if (v < 0) {
                hasNeg = true;
            }
            if (hasPos && hasNeg){
                return true;
            }
        }
        return false;
    }

    /** Convert annual nominal rate to effective monthly compounding rate. */
    static double annualToMonthly(double annual) {
        return Math.pow(1.0 + annual, 1.0 / 12.0) - 1.0;
    }

    /** Clone & adjust tenor by delta months. Repeats last month CF when extending. */
    static double[] adjustTenor(double[] cf, int deltaMonths) {
        if (deltaMonths == 0) {
            return cf.clone();
        }
        if (deltaMonths < 0) {
            int keep = Math.max(1, cf.length + deltaMonths); // keep at least t=0
            double[] out = new double[keep];
            System.arraycopy(cf, 0, out, 0, keep);
            return out;
        } else {
            double[] out = new double[cf.length + deltaMonths];
            System.arraycopy(cf, 0, out, 0, cf.length);
            double tail = cf[cf.length - 1];
            for (int i = cf.length; i < out.length; i++) {
                out[i] = tail;
            }
            return out;
        }
    }

    private static double round2(double v) {
        return BigDecimal
                .valueOf(v)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private static double round4(double v) {
        return BigDecimal
                .valueOf(v)
                .setScale(4, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
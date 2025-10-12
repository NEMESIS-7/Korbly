package com.arete.korbly.modules.valuation.mapper;

import com.arete.korbly.modules.syndication.domain.Tranche;
import com.arete.korbly.modules.syndication.exceptions.TrancheNotFound;
import com.arete.korbly.modules.syndication.persistence.TrancheRepository;
import com.arete.korbly.modules.valuation.domain.CashFlowAssumption;
import com.arete.korbly.modules.valuation.domain.ValuationAssumption;
import com.arete.korbly.modules.valuation.dto.ValuationPreviewRequest;

import java.sql.Timestamp;
import java.time.Instant;

public class ValuationMapper {
    private final TrancheRepository trancheRepository;

    public ValuationMapper(TrancheRepository trancheRepository) {
        this.trancheRepository = trancheRepository;
    }

    public ValuationAssumption toAssumptionEntity(ValuationPreviewRequest request) {
        Tranche tranche = trancheRepository.findById(request.trancheId())
                .orElseThrow(() -> new TrancheNotFound("Tranche with ID: " + request.trancheId() + " not found."));

        ValuationAssumption e = new ValuationAssumption();
        e.setTranche(tranche);

        e.setPrincipal(request.principalAmount());
        e.setAnnualRate(request.annualInterestRate());
        e.setTenorMonths(request.totalTenorInMonths());
        e.setAmortizationStructure(request.amortizationStructure());

        e.setGracePrincipalMonths(request.graceMonthsForPrincipal());
        e.setGraceInterestMonths(request.graceMonthsForInterest());

        e.setFeeUpfrontPct(request.upfrontFeePercent());
        e.setFeeServicingBps(request.servicingFeeBasisPoints());
        e.setFeeExitPct(request.exitFeePercent());

        e.setBalloonPercentOfOriginal(request.balloonPercentOfOriginal());
        e.setBalloonAmountAtMaturity(request.balloonAmountAtMaturity());
        e.setFixedMonthlyPayment(request.fixedMonthlyPayment());

        e.setNegativeAmortizationMonths(request.negativeAmortizationMonths());
        e.setMinPaymentPercentOfInterest(request.minPaymentPercentOfInterest());
        e.setMinPaymentAbsoluteAmount(request.minPaymentAbsoluteAmount());
        e.setNegativeAmortizationCapMultiple(request.negativeAmortizationCapMultiple());

        e.setScheduleStartDate(request.scheduleStartDate());
        e.setCurrency(request.currencyCode());

        e.setAnnualDiscountRate(request.investorDiscountRateAnnual());
        e.setScenarioLabel(request.scenarioLabel());

        e.setCreatedAt(Timestamp.from(Instant.now()));
        return e;
    }

    public static CashFlowAssumption toCashflowAssumption(ValuationAssumption a) {
        CashFlowAssumption c = new CashFlowAssumption();
        c.principal = a.getPrincipal();
        c.rateAnnual = a.getAnnualRate().doubleValue();
        c.tenorMonths = a.getTenorMonths();
        c.amortization = a.getAmortizationStructure();

        c.gracePrincipalMonths = a.getGracePrincipalMonths();
        c.graceInterestMonths = a.getGraceInterestMonths();

        c.feeUpfrontPct = a.getFeeUpfrontPct().doubleValue();
        c.feeServicingBps = a.getFeeServicingBps().doubleValue();
        c.feeExitPct = a.getFeeExitPct().doubleValue();

        c.balloonPct = (a.getBalloonPercentOfOriginal() == null) ? null : a.getBalloonPercentOfOriginal().doubleValue();
        c.balloonAmount = a.getBalloonAmountAtMaturity();
        c.fixedMonthlyPayment = a.getFixedMonthlyPayment();

        c.negAmMonths = a.getNegativeAmortizationMonths();
        c.minPaymentPctOfInterest = (a.getMinPaymentPercentOfInterest() == null) ? null : a.getMinPaymentPercentOfInterest().doubleValue();
        c.minPaymentAmount = a.getMinPaymentAbsoluteAmount();
        c.negAmCapPct = (a.getNegativeAmortizationCapMultiple() == null) ? null : a.getNegativeAmortizationCapMultiple().doubleValue();

        c.startDate = a.getScheduleStartDate();
        c.currency = a.getCurrency();

        return c;
    }
}

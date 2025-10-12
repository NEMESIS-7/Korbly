package com.arete.korbly.modules.valuation.service;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.persistence.AppUserRepository;
import com.arete.korbly.modules.valuation.domain.CashFlowAssumption;
import com.arete.korbly.modules.valuation.domain.ValuationAssumption;
import com.arete.korbly.modules.valuation.domain.ValuationResult;
import com.arete.korbly.modules.valuation.dto.*;
import com.arete.korbly.modules.valuation.mapper.ValuationMapper;
import com.arete.korbly.modules.valuation.persistence.ValuationAssumptionRepository;
import com.arete.korbly.modules.valuation.persistence.ValuationResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ValuationService implements IValuationService{
    private final ValuationAssumptionRepository assumptionRepo;
    private final ValuationResultRepository resultRepo;
    private final CashfFlowEngine cashflowEngine;
    private final AppUserRepository appUserRepo;
    private final ValuationMapper valuationMapper;

    public ValuationService(ValuationAssumptionRepository assumptionRepo,
                            ValuationResultRepository resultRepo,
                            CashfFlowEngine cashflowEngine,
                            AppUserRepository appUserRepo,
                            ValuationMapper valuationMapper) {
        this.assumptionRepo = assumptionRepo;
        this.resultRepo = resultRepo;
        this.cashflowEngine = cashflowEngine;
        this.appUserRepo = appUserRepo;
        this.valuationMapper = valuationMapper;
    }

    @Override
    @Transactional
    public ValuationPreviewResponse preview(ValuationPreviewRequest request, UUID requestedByUserId) {
        //todo check for user access rights

        ValuationAssumption assumption = valuationMapper.toAssumptionEntity(request);
        AppUser requester = appUserRepo.findById(requestedByUserId).orElse(null);
        assumption.setCreatedBy(requester);
        assumption = assumptionRepo.save(assumption); // save snapshot

        CashFlowAssumption cashflowAssumption = ValuationMapper.toCashflowAssumption(assumption);
        List<CashflowRow> cashflowSchedule = cashflowEngine.buildSchedule(cashflowAssumption);

        double[] investorCashflows = ValuationMath.toMonthlyCF(cashflowSchedule);

        double npv = ValuationMath.npvAnnual(investorCashflows, assumption.getAnnualDiscountRate().doubleValue());
        Double irr = ValuationMath.irrAnnual(investorCashflows);
        Double coc = ValuationMath.cashOnCash(investorCashflows);
        Integer payback = ValuationMath.paybackMonth(investorCashflows);

        List<double[]> npvVsDiscount = ValuationMath.sensitivityNPVbyDiscount(investorCashflows,
                assumption.getAnnualDiscountRate().doubleValue(),
                new int[]{-300, -200, -100, 0, 100, 200, 300});

        List<double[]> npvVsTenor = ValuationMath.sensitivityNPVbyTenor(investorCashflows,
                assumption.getAnnualDiscountRate().doubleValue(),
                new int[]{-12, -6, -3, 0, 3, 6, 12});

        String warning = (irr == null) ? "IRR is not defined (cashflows do not produce a valid root)." : null;

        return new ValuationPreviewResponse(
                BigDecimal.valueOf(npv).setScale(2),
                irr,
                coc,
                payback,
                cashflowSchedule,
                npvVsDiscount,
                npvVsTenor,
                warning
        );
    }

    @Override
    @Transactional
    public ValuationSummaryResponse commit(UUID valuationAssumptionId, UUID committedByUserId) {
        //todo check for user access rights

        ValuationAssumption assumption = assumptionRepo.findById(valuationAssumptionId)
                .orElseThrow(() -> new IllegalArgumentException("ValuationAssumption not found"));

        CashFlowAssumption cashflowAssumption = ValuationMapper.toCashflowAssumption(assumption);
        List<CashflowRow> schedule = cashflowEngine.buildSchedule(cashflowAssumption);
        double[] cf = ValuationMath.toMonthlyCF(schedule);

        double npv = ValuationMath.npvAnnual(cf, assumption.getAnnualDiscountRate().doubleValue());
        Double irr = ValuationMath.irrAnnual(cf);
        Double coc = ValuationMath.cashOnCash(cf);
        Integer payback = ValuationMath.paybackMonth(cf);

        // map schedule rows to embeddable line items
        List<CashFlowLineItem> lineItems = schedule.stream().map(r -> {
            CashFlowLineItem li = new CashFlowLineItem();
            li.setPeriodIndex(r.monthIndex());
            li.setPeriodEndDate(r.date());
            li.setOpeningBalance(r.opening());
            li.setInterestAccrued(r.interest());
            li.setPrincipalRepaid(r.principal());
            li.setFeesNetForPeriod(r.feesNet());
            li.setTotalCashflowForInvestor(r.total());
            li.setClosingBalance(r.closing());
            return li;
        }).toList();

        ValuationResult result = new ValuationResult();
        result.setValuationAssumptionId(assumption.getValuationId());
        result.setNetPresentValue(BigDecimal.valueOf(npv).setScale(2));
        result.setInternalRateOfReturnAnnual(irr);
        result.setCashOnCashMultiple(coc);
        result.setPaybackPeriodInMonths(payback);
        result.setCashflowSchedule(lineItems);
        result.setComputedAt(Timestamp.from(Instant.now()));
        result.setComputedByUserId(committedByUserId);

        // (Optional) add sensitivities here too if you want them persisted
        result = resultRepo.save(result);

        return new ValuationSummaryResponse(
                assumption.getValuationId(),
                result.getValuationResultId(),
                assumption.getScenarioLabel()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ValuationPreviewResponse getLatestByAssumption(UUID valuationAssumptionId) {
        var results = resultRepo.findByValuationAssumptionIdOrderByComputedAtDesc(valuationAssumptionId);
        if (results.isEmpty()){
            throw new IllegalArgumentException("No results for that assumption id");
        }

        ValuationResult latest = results.getFirst();

        return new ValuationPreviewResponse(
                latest.getNetPresentValue(),
                latest.getInternalRateOfReturnAnnual(),
                latest.getCashOnCashMultiple(),
                latest.getPaybackPeriodInMonths(),
                // turn persisted line items back to rows (optional; or return line items directly)
                latest.getCashflowSchedule().stream().map(li ->
                        new CashflowRow(
                                li.getPeriodIndex(),
                                li.getPeriodEndDate(),
                                li.getOpeningBalance(),
                                li.getInterestAccrued(),
                                li.getPrincipalRepaid(),
                                li.getFeesNetForPeriod(),
                                li.getTotalCashflowForInvestor(),
                                li.getClosingBalance()
                        )).toList(),
                null,
                null,
                null
        );
    }
}

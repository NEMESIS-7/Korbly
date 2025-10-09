package com.arete.korbly.modules.termsheet.dto;

import com.arete.korbly.modules.termsheet.enums.AmortizationStructure;
import com.arete.korbly.modules.termsheet.enums.GoverningLaw;
import com.arete.korbly.modules.termsheet.enums.Seniority;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TermSheetRequest {
    private UUID dealId;
    private UUID trancheId;
    private UUID smeId;

    private BigDecimal loanAmount;
    private Double interestRate;
    private LocalDate maturityDate;
    private AmortizationStructure amortizationStructure;
    private Boolean prepaymentOption;

    private Map<String, LocalDate> offeringPeriod;
    private Map<String, List<String>> guarantees;
    private Map<String, List<String>> collateral;

    private Seniority seniority;
    private Map<String, List<String>> covenants;
    private Map<String, List<String>> eventsOfDefault;

    private Double defaultRate;
    private Map<String, String> gracePeriods;
    private GoverningLaw governingLaw;

    public TermSheetRequest(UUID dealId, UUID trancheId, UUID smeId, BigDecimal loanAmount, Double interestRate, AmortizationStructure amortizationStructure, Boolean prepaymentOption, Map<String, LocalDate> offeringPeriod, Map<String, List<String>> guarantees, Map<String, List<String>> collateral, Seniority seniority, Map<String, List<String>> covenants, Map<String, List<String>> eventsOfDefault, Double defaultRate, Map<String, String> gracePeriods, GoverningLaw governingLaw,
                            LocalDate maturityDate) {
        this.dealId = dealId;
        this.trancheId = trancheId;
        this.smeId = smeId;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.amortizationStructure = amortizationStructure;
        this.prepaymentOption = prepaymentOption;
        this.offeringPeriod = offeringPeriod;
        this.guarantees = guarantees;
        this.collateral = collateral;
        this.seniority = seniority;
        this.covenants = covenants;
        this.eventsOfDefault = eventsOfDefault;
        this.defaultRate = defaultRate;
        this.gracePeriods = gracePeriods;
        this.governingLaw = governingLaw;
        this.maturityDate = maturityDate;
    }

    public UUID getDealId() {
        return dealId;
    }

    public void setDealId(UUID dealId) {
        this.dealId = dealId;
    }

    public UUID getTrancheId() {
        return trancheId;
    }

    public void setTrancheId(UUID trancheId) {
        this.trancheId = trancheId;
    }

    public UUID getSmeId() {
        return smeId;
    }

    public void setSmeId(UUID smeId) {
        this.smeId = smeId;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public AmortizationStructure getAmortizationStructure() {
        return amortizationStructure;
    }

    public void setAmortizationStructure(AmortizationStructure amortizationStructure) {
        this.amortizationStructure = amortizationStructure;
    }

    public Boolean getPrepaymentOption() {
        return prepaymentOption;
    }

    public void setPrepaymentOption(Boolean prepaymentOption) {
        this.prepaymentOption = prepaymentOption;
    }

    public Map<String, LocalDate> getOfferingPeriod() {
        return offeringPeriod;
    }

    public void setOfferingPeriod(Map<String, LocalDate> offeringPeriod) {
        this.offeringPeriod = offeringPeriod;
    }

    public Map<String, List<String>> getGuarantees() {
        return guarantees;
    }

    public void setGuarantees(Map<String, List<String>> guarantees) {
        this.guarantees = guarantees;
    }

    public Map<String, List<String>> getCollateral() {
        return collateral;
    }

    public void setCollateral(Map<String, List<String>> collateral) {
        this.collateral = collateral;
    }

    public Seniority getSeniority() {
        return seniority;
    }

    public void setSeniority(Seniority seniority) {
        this.seniority = seniority;
    }

    public Map<String, List<String>> getCovenants() {
        return covenants;
    }

    public void setCovenants(Map<String, List<String>> covenants) {
        this.covenants = covenants;
    }

    public Map<String, List<String>> getEventsOfDefault() {
        return eventsOfDefault;
    }

    public void setEventsOfDefault(Map<String, List<String>> eventsOfDefault) {
        this.eventsOfDefault = eventsOfDefault;
    }

    public Double getDefaultRate() {
        return defaultRate;
    }

    public void setDefaultRate(Double defaultRate) {
        this.defaultRate = defaultRate;
    }

    public Map<String, String> getGracePeriods() {
        return gracePeriods;
    }

    public void setGracePeriods(Map<String, String> gracePeriods) {
        this.gracePeriods = gracePeriods;
    }

    public GoverningLaw getGoverningLaw() {
        return governingLaw;
    }

    public void setGoverningLaw(GoverningLaw governingLaw) {
        this.governingLaw = governingLaw;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }
}

package com.arete.korbly.modules.sme.domain;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.shared.enums.SMEIndustry;
import com.arete.korbly.modules.shared.enums.SMERegion;
import jakarta.persistence.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Builder
public class SME {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID smeId;

    @Column(nullable = false, unique = true)
    private String companyName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SMEIndustry industry;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SMERegion region;

    @Column(nullable = false)
    private LocalDate dateEstablished;

    @Column(nullable = false, unique = true)
    private String websiteURL;

    @Column(nullable = false)
    private String businessDescription;

    @Column(nullable = false)
    private BigDecimal annualRevenue;

    @Column(nullable = false)
    private Integer numberOfEmployees;

    @Column(nullable = false)
    private BigDecimal monthlyRevenue;

    @Column(nullable = false)
    private BigDecimal requestedAmount;

    @Column(nullable = false)
    private String purposeOfFunding;

    @Column(nullable = false, unique = true)
    private String certOfIncorporation;

    @Column(nullable = false, unique = true)
    private String latestFinancialStatements;

    @Column(nullable = false, unique = true)
    private String businessPlan;

    @Column(nullable = false, unique = true)
    private String taxClearanceCert;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;

    @OneToOne
    @JoinColumn(name = "userId")
    private AppUser appUser;

    public SME() {
    }


    public SME(UUID smeId, String companyName, SMEIndustry industry, String registrationNumber, String phoneNumber, SMERegion region, LocalDate dateEstablished, String websiteURL, String businessDescription, BigDecimal annualRevenue, Integer numberOfEmployees, BigDecimal monthlyRevenue, BigDecimal requestedAmount, String purposeOfFunding, String certOfIncorporation, String latestFinancialStatements, String businessPlan, String taxClearanceCert, DeleteYn deleteYn, AppUser appUser) {
        this.smeId = smeId;
        this.companyName = companyName;
        this.industry = industry;
        this.registrationNumber = registrationNumber;
        this.phoneNumber = phoneNumber;
        this.region = region;
        this.dateEstablished = dateEstablished;
        this.websiteURL = websiteURL;
        this.businessDescription = businessDescription;
        this.annualRevenue = annualRevenue;
        this.numberOfEmployees = numberOfEmployees;
        this.monthlyRevenue = monthlyRevenue;
        this.requestedAmount = requestedAmount;
        this.purposeOfFunding = purposeOfFunding;
        this.certOfIncorporation = certOfIncorporation;
        this.latestFinancialStatements = latestFinancialStatements;
        this.businessPlan = businessPlan;
        this.taxClearanceCert = taxClearanceCert;
        this.deleteYn = deleteYn;
        this.appUser = appUser;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public DeleteYn getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(DeleteYn deleteYn) {
        this.deleteYn = deleteYn;
    }

    public UUID getSmeId() {
        return smeId;
    }

    public void setSmeId(UUID smeId) {
        this.smeId = smeId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public SMEIndustry getIndustry() {
        return industry;
    }

    public void setIndustry(SMEIndustry industry) {
        this.industry = industry;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public SMERegion getRegion() {
        return region;
    }

    public void setRegion(SMERegion region) {
        this.region = region;
    }

    public LocalDate getDateEstablished() {
        return dateEstablished;
    }

    public void setDateEstablished(LocalDate dateEstablished) {
        this.dateEstablished = dateEstablished;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    public BigDecimal getAnnualRevenue() {
        return annualRevenue;
    }

    public void setAnnualRevenue(BigDecimal annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public Integer getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(Integer numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public BigDecimal getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public String getPurposeOfFunding() {
        return purposeOfFunding;
    }

    public void setPurposeOfFunding(String purposeOfFunding) {
        this.purposeOfFunding = purposeOfFunding;
    }

    public String getCertOfIncorporation() {
        return certOfIncorporation;
    }

    public void setCertOfIncorporation(String certOfIncorporation) {
        this.certOfIncorporation = certOfIncorporation;
    }

    public String getLatestFinancialStatements() {
        return latestFinancialStatements;
    }

    public void setLatestFinancialStatements(String latestFinancialStatements) {
        this.latestFinancialStatements = latestFinancialStatements;
    }

    public String getBusinessPlan() {
        return businessPlan;
    }

    public void setBusinessPlan(String businessPlan) {
        this.businessPlan = businessPlan;
    }

    public String getTaxClearanceCert() {
        return taxClearanceCert;
    }

    public void setTaxClearanceCert(String taxClearanceCert) {
        this.taxClearanceCert = taxClearanceCert;
    }

    @PrePersist
    protected void onCreate(){
        this.deleteYn = DeleteYn.N;
    }

    @PreUpdate
    protected void onUpdate(){
        this.deleteYn = DeleteYn.N;
    }

}
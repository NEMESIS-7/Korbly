package com.arete.korbly.modules.investor.domain;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.shared.enums.InstitutionType;
import com.arete.korbly.modules.shared.enums.InvestmentFocus;
import com.arete.korbly.modules.shared.enums.RiskAppetite;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Builder
public class Investor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID investorId;

    @Enumerated(EnumType.STRING)
    private InstitutionType institutionType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Enumerated(EnumType.STRING)
    private Set<InvestmentFocus> investmentFocus;

    @Enumerated(EnumType.STRING)
    private RiskAppetite riskAppetite;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @Column(nullable = false, unique = true)
    private LocalDate dateEstablished;

    @Column(nullable = false, unique = true)
    private String institutionalAddress;

    @Column(nullable = false, unique = true)
    private String institutionName;


    @Column(nullable = false)
    private BigDecimal assetsUnderManagement;

    @Column(nullable = false)
    private BigDecimal minimumInvestment;

    @Column(nullable = false)
    private String certificateOfIncorporationURL;

    @Column(nullable = false)
    private String auditedFinancialStatementsURL;

    @Column(nullable = false)
    private String investmentPolicyStatementURL;

    @Column(nullable = false)
    private String boardResolutionURL;

    @OneToOne
    @JoinColumn(name = "userId")
    private AppUser appUser;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;


    public Investor(UUID investorId, InstitutionType institutionType, Set<InvestmentFocus> investmentFocus, RiskAppetite riskAppetite, String phoneNumber, String registrationNumber, LocalDate dateEstablished, String institutionalAddress, String institutionName, BigDecimal assetsUnderManagement, BigDecimal minimumInvestment, String certificateOfIncorporationURL, String auditedFinancialStatementsURL, String investmentPolicyStatementURL, String boardResolutionURL, AppUser appUser, DeleteYn deleteYn) {
        this.investorId = investorId;
        this.institutionType = institutionType;
        this.investmentFocus = investmentFocus;
        this.riskAppetite = riskAppetite;
        this.phoneNumber = phoneNumber;
        this.registrationNumber = registrationNumber;
        this.dateEstablished = dateEstablished;
        this.institutionalAddress = institutionalAddress;
        this.institutionName = institutionName;
        this.assetsUnderManagement = assetsUnderManagement;
        this.minimumInvestment = minimumInvestment;
        this.certificateOfIncorporationURL = certificateOfIncorporationURL;
        this.auditedFinancialStatementsURL = auditedFinancialStatementsURL;
        this.investmentPolicyStatementURL = investmentPolicyStatementURL;
        this.boardResolutionURL = boardResolutionURL;
        this.appUser = appUser;
        this.deleteYn = deleteYn;
    }

    @PrePersist
    protected void onCreate(){
        this.deleteYn = DeleteYn.N;
    }

    @PreUpdate
    protected void onUpdate(){
        this.deleteYn = DeleteYn.N;
    }



    public Investor() {
    }

    public DeleteYn getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(DeleteYn deleteYn) {
        this.deleteYn = deleteYn;
    }

    public UUID getInvestorId() {
        return investorId;
    }

    public void setInvestorId(UUID investorId) {
        this.investorId = investorId;
    }

    public InstitutionType getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(InstitutionType institutionType) {
        this.institutionType = institutionType;
    }

    public Set<InvestmentFocus> getInvestmentFocus() {
        return investmentFocus;
    }

    public void setInvestmentFocus(Set<InvestmentFocus> investmentFocus) {
        this.investmentFocus = investmentFocus;
    }

    public RiskAppetite getRiskAppetite() {
        return riskAppetite;
    }

    public void setRiskAppetite(RiskAppetite riskAppetite) {
        this.riskAppetite = riskAppetite;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public LocalDate getDateEstablished() {
        return dateEstablished;
    }

    public void setDateEstablished(LocalDate dateEstablished) {
        this.dateEstablished = dateEstablished;
    }

    public String getInstitutionalAddress() {
        return institutionalAddress;
    }

    public void setInstitutionalAddress(String institutionalAddress) {
        this.institutionalAddress = institutionalAddress;
    }

    public BigDecimal getAssetsUnderManagement() {
        return assetsUnderManagement;
    }

    public void setAssetsUnderManagement(BigDecimal assetsUnderManagement) {
        this.assetsUnderManagement = assetsUnderManagement;
    }

    public BigDecimal getMinimumInvestment() {
        return minimumInvestment;
    }

    public void setMinimumInvestment(BigDecimal minimumInvestment) {
        this.minimumInvestment = minimumInvestment;
    }

    public String getCertificateOfIncorporationURL() {
        return certificateOfIncorporationURL;
    }

    public void setCertificateOfIncorporationURL(String certificateOfIncorporationURL) {
        this.certificateOfIncorporationURL = certificateOfIncorporationURL;
    }

    public String getAuditedFinancialStatementsURL() {
        return auditedFinancialStatementsURL;
    }

    public void setAuditedFinancialStatementsURL(String auditedFinancialStatementsURL) {
        this.auditedFinancialStatementsURL = auditedFinancialStatementsURL;
    }

    public String getInvestmentPolicyStatementURL() {
        return investmentPolicyStatementURL;
    }

    public void setInvestmentPolicyStatementURL(String investmentPolicyStatementURL) {
        this.investmentPolicyStatementURL = investmentPolicyStatementURL;
    }

    public String getBoardResolutionURL() {
        return boardResolutionURL;
    }

    public void setBoardResolutionURL(String boardResolutionURL) {
        this.boardResolutionURL = boardResolutionURL;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }
}

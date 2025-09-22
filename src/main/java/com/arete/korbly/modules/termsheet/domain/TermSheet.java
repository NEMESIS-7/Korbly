package com.arete.korbly.modules.termsheet.domain;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.syndication.domain.Deal;
import com.arete.korbly.modules.syndication.domain.Tranche;
import com.arete.korbly.modules.termsheet.enums.AmortizationStructure;
import com.arete.korbly.modules.termsheet.enums.GoverningLaw;
import com.arete.korbly.modules.termsheet.enums.Seniority;
import com.arete.korbly.modules.termsheet.enums.TermSheetStatus;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Builder
public class TermSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID termSheetId;

    @OneToOne
    @JoinColumn(name = "deal_id")
    private Deal dealId;

    @OneToOne
    @JoinColumn(name = "tranche_id")
    private Tranche trancheId;

    @OneToOne
    @JoinColumn(name = "sme_id")
    private SME smeId;

    private BigDecimal loanAmount; //matches tranche amount

    private Double interestRate; //from tranche

    private LocalDate maturityDate; //derive from tranche tenor months

    @Enumerated(EnumType.STRING)
    private AmortizationStructure amortizationStructure;

    private Boolean prepaymentOption;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, LocalDate> offeringPeriod = new HashMap<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, List<String>> guarantees = new HashMap<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, List<String>> collateral = new HashMap<>();

    @Enumerated(EnumType.STRING)
    private Seniority seniority;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, List<String>> covenants = new HashMap<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, List<String>> eventsOfDefault = new HashMap<>();

    private Double defaultRate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> gracePeriods = new HashMap<>();

    @Enumerated(EnumType.STRING)
    private GoverningLaw governingLaw;

    @Enumerated(EnumType.STRING)
    private TermSheetStatus sheetStatus;

    private Integer sheetVersion;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp signedAt;

    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser createdBy;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;

    @OneToMany(mappedBy = "sheet", cascade = CascadeType.ALL)
    private List<ConditionsPrecedent> conditionsPrecedent;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private TermSheet parent;

    private Boolean isLatest;


    public TermSheet() {

    }

    //todo create shadow table, same fields but with a cloumn for the versions
    //todo the  table will have it's own id column, but allow duplicates for this...to track the different sheet versions

    @PrePersist
    protected void onCreate(){
        this.createdAt = Timestamp.from(Instant.now());
        this.updatedAt = Timestamp.from(Instant.now());
        this.deleteYn = DeleteYn.N;
        this.parent = parent == null ? this : parent;
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public TermSheet(UUID termSheetId,
                     Deal dealId,
                     Tranche trancheId,
                     SME smeId,
                     BigDecimal loanAmount,
                     Double interestRate,
                     LocalDate maturityDate,
                     AmortizationStructure amortizationStructure,
                     Boolean prepaymentOption,
                     Map<String, LocalDate> offeringPeriod,
                     Map<String, List<String>> guarantees,
                     Map<String, List<String>> collateral,
                     Seniority seniority,
                     Map<String, List<String>> covenants,
                     Map<String, List<String>> eventsOfDefault,
                     Double defaultRate,
                     Map<String, String> gracePeriods,
                     GoverningLaw governingLaw,
                     TermSheetStatus sheetStatus,
                     Integer sheetVersion,
                     Timestamp createdAt,
                     Timestamp updatedAt,
                     Timestamp signedAt,
                     AppUser createdBy,
                     DeleteYn deleteYn,
                     List<ConditionsPrecedent> conditionsPrecedent,
                     TermSheet parent,
                     Boolean isLatest) {
        this.termSheetId = termSheetId;
        this.dealId = dealId;
        this.trancheId = trancheId;
        this.smeId = smeId;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.maturityDate = maturityDate;
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
        this.sheetStatus = sheetStatus;
        this.sheetVersion = sheetVersion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.signedAt = signedAt;
        this.createdBy = createdBy;
        this.deleteYn = deleteYn;
        this.conditionsPrecedent = conditionsPrecedent;
        this.parent = parent;
        this.isLatest = isLatest;
    }


    public List<ConditionsPrecedent> getConditionsPrecedent() {
        return conditionsPrecedent;
    }

    public void setConditionsPrecedent(List<ConditionsPrecedent> conditionsPrecedent) {
        this.conditionsPrecedent = conditionsPrecedent;
    }

    public TermSheet getParentId() {
        return parent;
    }

    public void setParentId(TermSheet parent) {
        this.parent = parent;
    }

    public Boolean getLatest() {
        return isLatest;
    }

    public void setLatest(Boolean latest) {
        isLatest = latest;
    }

    public UUID getTermSheetId() {
        return termSheetId;
    }

    public void setTermSheetId(UUID termSheetId) {
        this.termSheetId = termSheetId;
    }

    public Deal getDealId() {
        return dealId;
    }

    public void setDealId(Deal dealId) {
        this.dealId = dealId;
    }

    public Tranche getTrancheId() {
        return trancheId;
    }

    public void setTrancheId(Tranche trancheId) {
        this.trancheId = trancheId;
    }

    public SME getSmeId() {
        return smeId;
    }

    public void setSmeId(SME smeId) {
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

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
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

    public TermSheetStatus getSheetStatus() {
        return sheetStatus;
    }

    public void setSheetStatus(TermSheetStatus sheetStatus) {
        this.sheetStatus = sheetStatus;
    }

    public Integer getSheetVersion() {
        return sheetVersion;
    }

    public void setSheetVersion(Integer sheetVersion) {
        this.sheetVersion = sheetVersion;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getSignedAt() {
        return signedAt;
    }

    public void setSignedAt(Timestamp signedAt) {
        this.signedAt = signedAt;
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUser createdBy) {
        this.createdBy = createdBy;
    }

    public DeleteYn getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(DeleteYn deleteYn) {
        this.deleteYn = deleteYn;
    }
}
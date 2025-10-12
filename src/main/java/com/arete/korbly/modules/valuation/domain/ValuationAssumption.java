package com.arete.korbly.modules.valuation.domain;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.syndication.domain.Tranche;
import com.arete.korbly.modules.syndication.enums.DealCurrency;
import com.arete.korbly.modules.termsheet.enums.AmortizationStructure;
import com.arete.korbly.modules.valuation.enums.ValuationSource;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_tranche", columnList = "tranche_id"),
                @Index(name = "idx_asof", columnList = "as_of")
        }
)
@Builder
public class ValuationAssumption {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID valuationId;

    @ManyToOne
    @JoinColumn(name = "tranche_id")
    private Tranche tranche;

    @Column(nullable = false)
    private Timestamp asOf;

    @Column(nullable = false)
    private BigDecimal principal;

    @Column(nullable = false)
    private double annualRate;

    @Column(nullable = false)
    private int tenorMonths;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AmortizationStructure amortizationStructure;

    @Column(nullable = false)
    private int gracePrincipalMonths = 0;

    @Column(nullable = false)
    private int graceInterestMonths = 0;

    @Column(nullable = false)
    private BigDecimal feeUpfrontPct = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal feeServicingBps = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal feeExitPct = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal annualDiscountRate;

    @Enumerated(EnumType.STRING)
    private DealCurrency currency = DealCurrency.GHS;

    @Enumerated(EnumType.STRING)
    private ValuationSource source;

    @OneToOne
    @JoinColumn(name = "created_by")
    private AppUser createdBy;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn = DeleteYn.N;

    public ValuationAssumption() {
    }

    public ValuationAssumption(UUID valuationId, Tranche tranche, Timestamp asOf, BigDecimal principal, double annualRate, int tenorMonths, AmortizationStructure amortizationStructure, int gracePrincipalMonths, int graceInterestMonths, BigDecimal feeUpfrontPct, BigDecimal feeServicingBps, BigDecimal feeExitPct, BigDecimal annualDiscountRate, DealCurrency currency, ValuationSource source, AppUser createdBy, Timestamp createdAt, Timestamp updatedAt, DeleteYn deleteYn) {
        this.valuationId = valuationId;
        this.tranche = tranche;
        this.asOf = asOf;
        this.principal = principal;
        this.annualRate = annualRate;
        this.tenorMonths = tenorMonths;
        this.amortizationStructure = amortizationStructure;
        this.gracePrincipalMonths = gracePrincipalMonths;
        this.graceInterestMonths = graceInterestMonths;
        this.feeUpfrontPct = feeUpfrontPct;
        this.feeServicingBps = feeServicingBps;
        this.feeExitPct = feeExitPct;
        this.annualDiscountRate = annualDiscountRate;
        this.currency = currency;
        this.source = source;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleteYn = deleteYn;
    }

    public UUID getValuationId() {
        return valuationId;
    }

    public void setValuationId(UUID valuationId) {
        this.valuationId = valuationId;
    }

    public Tranche getTranche() {
        return tranche;
    }

    public void setTranche(Tranche tranche) {
        this.tranche = tranche;
    }

    public Timestamp getAsOf() {
        return asOf;
    }

    public void setAsOf(Timestamp asOf) {
        this.asOf = asOf;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public double getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(double annualRate) {
        this.annualRate = annualRate;
    }

    public int getTenorMonths() {
        return tenorMonths;
    }

    public void setTenorMonths(int tenorMonths) {
        this.tenorMonths = tenorMonths;
    }

    public AmortizationStructure getAmortizationStructure() {
        return amortizationStructure;
    }

    public void setAmortizationStructure(AmortizationStructure amortizationStructure) {
        this.amortizationStructure = amortizationStructure;
    }

    public int getGracePrincipalMonths() {
        return gracePrincipalMonths;
    }

    public void setGracePrincipalMonths(int gracePrincipalMonths) {
        this.gracePrincipalMonths = gracePrincipalMonths;
    }

    public int getGraceInterestMonths() {
        return graceInterestMonths;
    }

    public void setGraceInterestMonths(int graceInterestMonths) {
        this.graceInterestMonths = graceInterestMonths;
    }

    public BigDecimal getFeeUpfrontPct() {
        return feeUpfrontPct;
    }

    public void setFeeUpfrontPct(BigDecimal feeUpfrontPct) {
        this.feeUpfrontPct = feeUpfrontPct;
    }

    public BigDecimal getFeeServicingBps() {
        return feeServicingBps;
    }

    public void setFeeServicingBps(BigDecimal feeServicingBps) {
        this.feeServicingBps = feeServicingBps;
    }

    public BigDecimal getFeeExitPct() {
        return feeExitPct;
    }

    public void setFeeExitPct(BigDecimal feeExitPct) {
        this.feeExitPct = feeExitPct;
    }

    public BigDecimal getAnnualDiscountRate() {
        return annualDiscountRate;
    }

    public void setAnnualDiscountRate(BigDecimal annualDiscountRate) {
        this.annualDiscountRate = annualDiscountRate;
    }

    public DealCurrency getDealCurrency() {
        return currency;
    }

    public void setDealCurrency(DealCurrency currency) {
        this.currency = currency;
    }

    public ValuationSource getSource() {
        return source;
    }

    public void setSource(ValuationSource source) {
        this.source = source;
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUser createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public DeleteYn getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(DeleteYn deleteYn) {
        this.deleteYn = deleteYn;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
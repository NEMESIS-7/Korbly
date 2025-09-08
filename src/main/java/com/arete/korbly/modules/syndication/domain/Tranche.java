package com.arete.korbly.modules.syndication.domain;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.syndication.enums.TrancheStatus;
import com.arete.korbly.modules.syndication.enums.TrancheType;
import jakarta.persistence.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Builder
public class Tranche {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID trancheId;

    @Enumerated(EnumType.STRING)
    private TrancheType trancheType;

    @Column(nullable = false, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, scale = 2)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private Integer tenorMonths;

    private Boolean isAnchor;

    @ManyToOne
    @JoinColumn(name = "dealId")
    private Deal deal;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;

    @ManyToOne
    @JoinColumn(name = "createdById")
    private AppUser createdBy;

    @Enumerated(EnumType.STRING)
    private TrancheStatus trancheStatus;

    private Boolean isAllocated;


    public Tranche(UUID trancheId,
                   TrancheType trancheType,
                   BigDecimal amount,
                   BigDecimal interestRate,
                   Integer tenorMonths,
                   Boolean isAnchor,
                   Deal deal,
                   Timestamp createdAt,
                   Timestamp updatedAt,
                   DeleteYn deleteYn,
                   AppUser createdBy,
                   TrancheStatus trancheStatus,
                   Boolean isAllocated) {
        this.trancheId = trancheId;
        this.trancheType = trancheType;
        this.amount = amount;
        this.interestRate = interestRate;
        this.tenorMonths = tenorMonths;
        this.isAnchor = isAnchor;
        this.deal = deal;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleteYn = deleteYn;
        this.createdBy = createdBy;
        this.trancheStatus = trancheStatus;
        this.isAllocated = isAllocated;
    }

    public Tranche() {
    }

    public Boolean getAllocated() {
        return isAllocated;
    }

    public void setAllocated(Boolean allocated) {
        isAllocated = allocated;
    }

    public TrancheStatus getTrancheStatus() {
        return trancheStatus;
    }

    public void setTrancheStatus(TrancheStatus trancheStatus) {
        this.trancheStatus = trancheStatus;
    }

    public UUID getTrancheId() {
        return trancheId;
    }

    public void setTrancheId(UUID trancheId) {
        this.trancheId = trancheId;
    }

    public TrancheType getTrancheType() {
        return trancheType;
    }

    public void setTrancheType(TrancheType trancheType) {
        this.trancheType = trancheType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getTenorMonths() {
        return tenorMonths;
    }

    public void setTenorMonths(Integer tenorMonths) {
        this.tenorMonths = tenorMonths;
    }

    public Boolean isAnchor() {
        return isAnchor;
    }

    public void setAnchor(Boolean anchor) {
        isAnchor = anchor;
    }

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
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

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUser createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getAnchor() {
        return isAnchor;
    }

    public DeleteYn getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(DeleteYn deleteYn) {
        this.deleteYn = deleteYn;
    }

    @PrePersist
    protected void onCreate(){
        this.createdAt = Timestamp.from(Instant.now());
        this.updatedAt = Timestamp.from(Instant.now());
        this.deleteYn = DeleteYn.N;
        this.trancheStatus = TrancheStatus.OPEN;
        this.isAllocated = Boolean.FALSE;
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = Timestamp.from(Instant.now());
    }
}

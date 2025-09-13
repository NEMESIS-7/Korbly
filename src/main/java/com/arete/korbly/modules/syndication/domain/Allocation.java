package com.arete.korbly.modules.syndication.domain;

import com.arete.korbly.modules.investor.domain.Investor;
import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.syndication.enums.AllocationStatus;
import jakarta.persistence.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_investor", columnList = "investor_id"),
                @Index(name = "idx_tranche", columnList = "tranche_id")
        }
)
@Builder
public class Allocation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID allocationId;

    @ManyToOne
    @JoinColumn(name = "tranche_Id")
    private Tranche trancheId;

    @ManyToOne
    @JoinColumn(name = "investor_id")
    private Investor investorId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private AllocationStatus allocationStatus;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @OneToOne
    private AppUser confirmedBy;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;

    private Timestamp confirmedAt;

    @PrePersist
    protected void onCreate(){
        this.deleteYn = DeleteYn.N;
        this.createdAt = Timestamp.from(Instant.now());
        this.updatedAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public Allocation(UUID allocationId, Tranche trancheId, Investor investorId, BigDecimal amount, AllocationStatus allocationStatus, Timestamp createdAt, Timestamp updatedAt,
                      AppUser confirmedBy,
                      DeleteYn deleteYn,
                      Timestamp confirmedAt) {
        this.allocationId = allocationId;
        this.trancheId = trancheId;
        this.investorId = investorId;
        this.amount = amount;
        this.allocationStatus = allocationStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.confirmedBy = confirmedBy;
        this.deleteYn = deleteYn;
        this.confirmedAt = confirmedAt;
    }

    public DeleteYn getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(DeleteYn deleteYn) {
        this.deleteYn = deleteYn;
    }

    public Allocation() {
    }

    public UUID getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(UUID allocationId) {
        this.allocationId = allocationId;
    }

    public Tranche getTrancheId() {
        return trancheId;
    }

    public void setTrancheId(Tranche trancheId) {
        this.trancheId = trancheId;
    }

    public Investor getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Investor investorId) {
        this.investorId = investorId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AllocationStatus getAllocationStatus() {
        return allocationStatus;
    }

    public void setAllocationStatus(AllocationStatus allocationStatus) {
        this.allocationStatus = allocationStatus;
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

    public AppUser getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(AppUser confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public Timestamp getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(Timestamp confirmedAt) {
        this.confirmedAt = confirmedAt;
    }
}

package com.arete.korbly.modules.syndication.domain;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.shared.enums.SMEIndustry;
import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.syndication.enums.DealCurrency;
import com.arete.korbly.modules.syndication.enums.DealStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID dealId;

    @Size(max = 150)
    @Column(nullable = false, unique = true)
    private String dealTitle;


    @Size(max = 300)
    @Column(nullable = false, unique = true)
    private String dealDescription;

    @Enumerated(EnumType.STRING)
    private SMEIndustry dealSector;

    @Column(scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private DealCurrency currency;

    @Enumerated(EnumType.STRING)
    private DealStatus dealStatus;

    @ManyToOne
    @JoinColumn(name = "smeId")
    private SME smeInvolved;

    @OneToMany(mappedBy = "deal", cascade = CascadeType.ALL)
    private List<Tranche> tranches;


    private Timestamp createdAt;
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "createdById")
    private AppUser createdBy;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;

    @PrePersist
    protected void onCreate(){
        this.createdAt = Timestamp.from(Instant.now());
        this.updatedAt = Timestamp.from(Instant.now());
        this.deleteYn = DeleteYn.N;
        this.tranches = new ArrayList<>();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public Deal(UUID dealId,
                String dealTitle,
                String dealDescription,
                SMEIndustry dealSector,
                BigDecimal totalAmount,
                DealCurrency currency,
                DealStatus dealStatus,
                SME smeInvolved,
                List<Tranche> tranches,
                Timestamp createdAt,
                Timestamp updatedAt,
                AppUser createdBy,
                DeleteYn deleteYn) {
        this.dealId = dealId;
        this.dealTitle = dealTitle;
        this.dealDescription = dealDescription;
        this.dealSector = dealSector;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.dealStatus = dealStatus;
        this.smeInvolved = smeInvolved;
        this.tranches = tranches;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.deleteYn = deleteYn;
    }

    public Deal() {
    }

    public UUID getDealId() {
        return dealId;
    }

    public void setDealId(UUID dealId) {
        this.dealId = dealId;
    }

    public String getDealTitle() {
        return dealTitle;
    }


    public String getDealDescription() {
        return dealDescription;
    }


    public SMEIndustry getDealSector() {
        return dealSector;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    public void setDealDescription(String dealDescription) {
        this.dealDescription = dealDescription;
    }

    public void setDealSector(SMEIndustry dealSector) {
        this.dealSector = dealSector;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setCurrency(DealCurrency currency) {
        this.currency = currency;
    }

    public void setSmeInvolved(SME smeInvolved) {
        this.smeInvolved = smeInvolved;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public DealCurrency getCurrency() {
        return currency;
    }



    public DealStatus getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(DealStatus dealStatus) {
        this.dealStatus = dealStatus;
    }

    public SME getSmeInvolved() {
        return smeInvolved;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
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

    public DeleteYn getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(DeleteYn deleteYn) {
        this.deleteYn = deleteYn;
    }

    public List<Tranche> getTranches() {
        return tranches;
    }

    public void setTranches(List<Tranche> tranches) {
        this.tranches = tranches;
    }
}

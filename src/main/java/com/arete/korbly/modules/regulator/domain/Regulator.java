package com.arete.korbly.modules.regulator.domain;


import com.arete.korbly.modules.regulator.enums.RegulatorStatus;
import com.arete.korbly.modules.regulator.enums.RegulatorType;
import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import jakarta.persistence.*;
import lombok.Builder;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Builder
public class Regulator {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID regulatorId;

    @Column(nullable = false, unique = true)
    private String regulatorName;

    @Column(nullable = false)
    private String regulatorJurisdiction;

    @Enumerated(EnumType.STRING)
    private RegulatorType regulatorType;

    @Enumerated(EnumType.STRING)
    private RegulatorStatus regulatorStatus;

    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;

    @OneToOne
    @JoinColumn(name = "created_by")
    private AppUser createdBy;

    @PrePersist
    protected void onCreate(){
        this.createdAt = Timestamp.from(Instant.now());
        this.createdAt = Timestamp.from(Instant.now());
        this.deleteYn = DeleteYn.N;
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = Timestamp.from(Instant.now());
    }


    public Regulator(UUID regulatorId, String regulatorName, String regulatorJurisdiction, RegulatorType regulatorType, RegulatorStatus regulatorStatus, AppUser appUser, Timestamp createdAt, Timestamp updatedAt, DeleteYn deleteYn, AppUser createdBy) {
        this.regulatorId = regulatorId;
        this.regulatorName = regulatorName;
        this.regulatorJurisdiction = regulatorJurisdiction;
        this.regulatorType = regulatorType;
        this.regulatorStatus = regulatorStatus;
        this.appUser = appUser;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleteYn = deleteYn;
        this.createdBy = createdBy;
    }

    public Regulator() {
    }

    public UUID getRegulatorId() {
        return regulatorId;
    }

    public void setRegulatorId(UUID regulatorId) {
        this.regulatorId = regulatorId;
    }

    public String getName() {
        return regulatorName;
    }

    public void setName(String regulatorName) {
        this.regulatorName = regulatorName;
    }

    public String getJurisdiction() {
        return regulatorJurisdiction;
    }

    public void setJurisdiction(String regulatorJurisdiction) {
        this.regulatorJurisdiction = regulatorJurisdiction;
    }

    public RegulatorType getRegulatorType() {
        return regulatorType;
    }

    public void setRegulatorType(RegulatorType regulatorType) {
        this.regulatorType = regulatorType;
    }

    public RegulatorStatus getRegulatorStatus() {
        return regulatorStatus;
    }

    public void setRegulatorStatus(RegulatorStatus regulatorStatus) {
        this.regulatorStatus = regulatorStatus;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
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

    public DeleteYn getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(DeleteYn deleteYn) {
        this.deleteYn = deleteYn;
    }

    public String getRegulatorName() {
        return regulatorName;
    }

    public void setRegulatorName(String regulatorName) {
        this.regulatorName = regulatorName;
    }

    public String getRegulatorJurisdiction() {
        return regulatorJurisdiction;
    }

    public void setRegulatorJurisdiction(String regulatorJurisdiction) {
        this.regulatorJurisdiction = regulatorJurisdiction;
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUser createdBy) {
        this.createdBy = createdBy;
    }
}

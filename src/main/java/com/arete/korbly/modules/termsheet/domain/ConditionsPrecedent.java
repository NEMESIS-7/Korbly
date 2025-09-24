package com.arete.korbly.modules.termsheet.domain;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.termsheet.enums.CPStatus;
import jakarta.persistence.*;
import lombok.Builder;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Builder
public class ConditionsPrecedent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cpId;

    @ManyToOne
    @JoinColumn(name = "sheet_id")
    private TermSheet sheet;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Boolean required;

    @Enumerated(EnumType.STRING)
    private CPStatus status;

    private String evidenceFileKey;

    @Column(columnDefinition = "TEXT")
    private String note;

    @OneToOne
    @JoinColumn(name = "approved_by_user_id")
    private AppUser approvedBy;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Column(columnDefinition = "TEXT")
    private String waiverReason;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;

    public ConditionsPrecedent() {
    }

    public ConditionsPrecedent(UUID cpId,
                               TermSheet sheet,
                               String title,
                               String description,
                               Boolean required,
                               CPStatus status,
                               String evidenceFileKey,
                               String note,
                               AppUser approvedBy,
                               Timestamp createdAt,
                               Timestamp updatedAt,
                               String waiverReason,
                               DeleteYn deleteYn) {
        this.cpId = cpId;
        this.sheet = sheet;
        this.title = title;
        this.description = description;
        this.required = required;
        this.status = status;
        this.evidenceFileKey = evidenceFileKey;
        this.note = note;
        this.approvedBy = approvedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.waiverReason = waiverReason;
        this.deleteYn = deleteYn;
    }

    public ConditionsPrecedent(TermSheet sheet,
                               String title,
                               String description,
                               Boolean required,
                               CPStatus status,
                               String evidenceFileKey,
                               String note,
                               AppUser approvedBy,
                               Timestamp createdAt,
                               String waiverReason,
                               DeleteYn deleteYn) {
        this.sheet = sheet;
        this.title = title;
        this.description = description;
        this.required = required;
        this.status = status;
        this.evidenceFileKey = evidenceFileKey;
        this.note = note;
        this.approvedBy = approvedBy;
        this.createdAt = createdAt;
        this.waiverReason = waiverReason;
        this.deleteYn = deleteYn;
    }

    @PrePersist
    protected void onCreate() {
        this.deleteYn = DeleteYn.N;
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public DeleteYn getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(DeleteYn deleteYn) {
        this.deleteYn = deleteYn;
    }

    public UUID getCpId() {
        return cpId;
    }

    public void setCpId(UUID cpId) {
        this.cpId = cpId;
    }

    public TermSheet getSheetId() {
        return sheet;
    }

    public void setSheetId(TermSheet sheet) {
        this.sheet = sheet;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public CPStatus getStatus() {
        return status;
    }

    public void setStatus(CPStatus status) {
        this.status = status;
    }

    public String getEvidenceFileKey() {
        return evidenceFileKey;
    }

    public void setEvidenceFileKey(String evidenceFileKey) {
        this.evidenceFileKey = evidenceFileKey;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public AppUser getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(AppUser approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getWaiverReason() {
        return waiverReason;
    }

    public void setWaiverReason(String waiverReason) {
        this.waiverReason = waiverReason;
    }

    public TermSheet getSheet() {
        return sheet;
    }

    public void setSheet(TermSheet sheet) {
        this.sheet = sheet;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}

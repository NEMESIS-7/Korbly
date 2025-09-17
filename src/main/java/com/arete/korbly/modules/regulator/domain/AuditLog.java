package com.arete.korbly.modules.regulator.domain;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.shared.enums.UserType;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID logId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser userId;

    private UUID actorId;

    private UserType actorRole;

    private String entityType;

    private String action;

    private Timestamp timestamp;

    private String ipAddress;

    private String requestId;

    private UUID entityId;

    private Timestamp createdOn;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;

    @PrePersist
    protected void onCreate(){
        this.createdOn = Timestamp.from(Instant.now());
        this.timestamp = Timestamp.from(Instant.now());
        this.deleteYn = DeleteYn.N;
    }

    public AuditLog(UUID logId,
                    AppUser userId,
                    UUID actorId,
                    UserType actorRole,
                    String entityType,
                    String action,
                    Timestamp timestamp,
                    String ipAddress,
                    String requestId, UUID entityId,
                    Timestamp createdOn, DeleteYn deleteYn) {
        this.logId = logId;
        this.userId = userId;
        this.actorId = actorId;
        this.actorRole = actorRole;
        this.entityType = entityType;
        this.action = action;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
        this.requestId = requestId;
        this.entityId = entityId;
        this.createdOn = createdOn;
        this.deleteYn = deleteYn;
    }

    public AuditLog() {

    }

    public UUID getLogId() {
        return logId;
    }

    public void setLogId(UUID logId) {
        this.logId = logId;
    }

    public AppUser getUserId() {
        return userId;
    }

    public void setUserId(AppUser userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public UUID getActorId() {
        return actorId;
    }

    public void setActorId(UUID actorId) {
        this.actorId = actorId;
    }

    public UserType getActorRole() {
        return actorRole;
    }

    public void setActorRole(UserType actorRole) {
        this.actorRole = actorRole;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public DeleteYn getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(DeleteYn deleteYn) {
        this.deleteYn = deleteYn;
    }
}

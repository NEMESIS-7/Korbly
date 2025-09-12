package com.arete.korbly.modules.regulator.domain;

import com.arete.korbly.modules.shared.domain.AppUser;
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

    private Timestamp createdOn;

    @PrePersist
    protected void onCreate(){
        this.createdOn = Timestamp.from(Instant.now());
    }

    public AuditLog(UUID logId,
                    AppUser userId,
                    Timestamp createdOn) {
        this.logId = logId;
        this.userId = userId;
        this.createdOn = createdOn;
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
}

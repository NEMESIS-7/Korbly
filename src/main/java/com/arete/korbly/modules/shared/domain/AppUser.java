package com.arete.korbly.modules.shared.domain;

import com.arete.korbly.modules.shared.enums.DeleteYn;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.arete.korbly.modules.shared.enums.UserType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;


@Getter
@Setter
@Entity
@Builder
public class AppUser  {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String primaryContactEmail;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private Boolean isVerified;

    private Timestamp createdOn;

    private Timestamp updatedOn;

    private Timestamp lastLogin;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;

    public AppUser(UUID userId, String primaryContactEmail, UserType userType, Boolean isVerified, Timestamp createdOn, Timestamp updatedOn, Timestamp lastLogin, DeleteYn deleteYn) {
        this.userId = userId;
        this.primaryContactEmail = primaryContactEmail;
        this.userType = userType;
        this.isVerified = isVerified;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.lastLogin = lastLogin;
        this.deleteYn = deleteYn;
    }



    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getPrimaryContactEmail() {
        return primaryContactEmail;
    }

    public void setPrimaryContactEmail(String primaryContactEmail) {
        this.primaryContactEmail = primaryContactEmail;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public DeleteYn getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(DeleteYn deleteYn) {
        this.deleteYn = deleteYn;
    }

    public AppUser() {
    }

    @PrePersist
    protected void onCreate(){
        this.isVerified = false;
        this.createdOn = Timestamp.from(Instant.now());
        this.deleteYn = DeleteYn.N;
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedOn = Timestamp.from(Instant.now());
    }

}

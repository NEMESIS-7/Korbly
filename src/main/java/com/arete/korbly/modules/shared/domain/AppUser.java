package com.arete.korbly.modules.shared.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.arete.korbly.modules.shared.enums.UserType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;


@Getter
@Setter
@Entity
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


    @PrePersist
    protected void onCreate(){
        this.isVerified = false;
        this.createdOn = Timestamp.from(Instant.now());
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedOn = Timestamp.from(Instant.now());
    }

}

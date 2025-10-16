package com.arete.korbly.modules.syndication.domain;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.syndication.enums.TrancheStatus;
import com.arete.korbly.modules.syndication.enums.TrancheType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tranche {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID trancheId;

    @Enumerated(EnumType.STRING)
    private TrancheType trancheType;

    @Column(nullable = false, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, scale = 2)
    private Double interestRate;

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

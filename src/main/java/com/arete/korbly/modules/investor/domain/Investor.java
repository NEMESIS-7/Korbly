package com.arete.korbly.modules.investor.domain;

import jakarta.persistence.*;
import lombok.*;
import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.InstitutionType;
import com.arete.korbly.modules.shared.enums.InvestmentFocus;
import com.arete.korbly.modules.shared.enums.RiskAppetite;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Investor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID investorId;

    @Enumerated(EnumType.STRING)
    private InstitutionType institutionType;

    @Enumerated(EnumType.STRING)
    private InvestmentFocus investmentFocus;

    @Enumerated(EnumType.STRING)
    private RiskAppetite riskAppetite;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @Column(nullable = false, unique = true)
    private LocalDate dateEstablished;

    @Column(nullable = false, unique = true)
    private String institutionalAddress;

    @CreationTimestamp
    private Timestamp createdOn;

    @UpdateTimestamp
    private Timestamp updatedOn;

    @Column(nullable = false)
    private BigDecimal assetsUnderManagement;

    @Column(nullable = false)
    private BigDecimal minimumInvestment;

    @Column(nullable = false)
    private String certificateOfIncorporationURL;

    @Column(nullable = false)
    private String auditedFinancialStatementsURL;

    @Column(nullable = false)
    private String investmentPolicyStatementURL;

    @Column(nullable = false)
    private String boardResolutionURL;

    @OneToOne
    @JoinColumn(name = "userId")
    private AppUser appUser;

}

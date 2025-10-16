package com.arete.korbly.modules.credit.domain;

import com.arete.korbly.modules.credit.enums.ESGRiskRating;
import com.arete.korbly.modules.credit.dto.FinancialsDTO;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.sme.domain.SME;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreditMemo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID creditMemoId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sme_id", nullable = false)
    private SME sme;

    @Column(precision = 5, scale = 2)
    private BigDecimal altmanScore;

    private BigDecimal ohlsonScore;

    private Boolean fxMisMatchFlag;
    private Boolean weakCoverageFlag;
    private Boolean cyclicalVulnerabilityFlag;

    @JdbcTypeCode(SqlTypes.JSON)
    private FinancialsDTO rawFinancials;

    private Double dscr;

    private Double icr;

    @Enumerated(EnumType.STRING)
    private ESGRiskRating esgRiskRating;

    private Timestamp evaluatedAt;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;

    private Boolean isLatest = Boolean.TRUE;

    @Column(precision = 10, scale = 4)
    private BigDecimal riskComposite;

    @PrePersist
    protected void onCreate(){
        this.deleteYn = DeleteYn.N;
    }

    @PreUpdate
    protected void onUpdate(){
        this.deleteYn = DeleteYn.N;
    }
}

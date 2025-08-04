package com.arete.korbly.modules.credit.domain;

import com.arete.korbly.modules.credit.ESGRiskRating;
import com.arete.korbly.modules.credit.dto.FinancialsDTO;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.sme.domain.SME;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Builder
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

    private ESGRiskRating esgRiskRating;

    private Timestamp evaluatedAt;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;

    public CreditMemo(UUID creditMemoId, SME sme, BigDecimal altmanScore, BigDecimal ohlsonScore, Boolean fxMisMatchFlag, Boolean weakCoverageFlag, Boolean cyclicalVulnerabilityFlag, FinancialsDTO rawFinancials, ESGRiskRating esgRiskRating, Timestamp evaluatedAt, DeleteYn deleteYn) {
        this.creditMemoId = creditMemoId;
        this.sme = sme;
        this.altmanScore = altmanScore;
        this.ohlsonScore = ohlsonScore;
        this.fxMisMatchFlag = fxMisMatchFlag;
        this.weakCoverageFlag = weakCoverageFlag;
        this.cyclicalVulnerabilityFlag = cyclicalVulnerabilityFlag;
        this.rawFinancials = rawFinancials;
        this.esgRiskRating = esgRiskRating;
        this.evaluatedAt = evaluatedAt;
        this.deleteYn = deleteYn;
    }

    public CreditMemo() {
    }

    @PrePersist
    protected void onCreate(){
        this.deleteYn = DeleteYn.N;
    }

    @PreUpdate
    protected void onUpdate(){
        this.deleteYn = DeleteYn.N;
    }

    public UUID getCreditMemoId() {
        return creditMemoId;
    }

    public void setCreditMemoId(UUID creditMemoId) {
        this.creditMemoId = creditMemoId;
    }

    public SME getSme() {
        return sme;
    }

    public void setSme(SME sme) {
        this.sme = sme;
    }

    public BigDecimal getAltmanScore() {
        return altmanScore;
    }

    public void setAltmanScore(BigDecimal altmanScore) {
        this.altmanScore = altmanScore;
    }

    public BigDecimal getOhlsonScore() {
        return ohlsonScore;
    }

    public void setOhlsonScore(BigDecimal ohlsonScore) {
        this.ohlsonScore = ohlsonScore;
    }

    public Boolean isFxMisMatchFlag() {
        return fxMisMatchFlag;
    }

    public void setFxMisMatchFlag(Boolean fxMisMatchFlag) {
        this.fxMisMatchFlag = fxMisMatchFlag;
    }

    public Boolean isWeakCoverage() {
        return weakCoverageFlag;
    }

    public void setWeakCoverageFlag(Boolean weakCoverageFlag) {
        this.weakCoverageFlag = weakCoverageFlag;
    }

    public Boolean isCyclicalVulnerabilityFlag() {
        return cyclicalVulnerabilityFlag;
    }

    public void setCyclicalVulnerabilityFlag(Boolean cyclicalVulnerabilityFlag) {
        this.cyclicalVulnerabilityFlag = cyclicalVulnerabilityFlag;
    }

    public ESGRiskRating getEsgRiskRating() {
        return esgRiskRating;
    }

    public void setEsgRiskRating(ESGRiskRating esgRiskRating) {
        this.esgRiskRating = esgRiskRating;
    }

    public Timestamp getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(Timestamp evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }
}

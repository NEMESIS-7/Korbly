package com.arete.korbly.modules.valuation.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class ValuationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID resultId;

    @OneToOne
    @JoinColumn(name = "assumption_id")
    private ValuationAssumption valuationAssumption;

    private BigDecimal netPresentValue;

    private BigDecimal internalRateReturn;

    private BigDecimal cashOnCash;

    private int payBackMonth;

    @JdbcTypeCode(SqlTypes.JSON)
    private String jsonCashFlows;

    @JdbcTypeCode(SqlTypes.JSON)
    private String jsonSensitivities;

    @CreationTimestamp
    private Timestamp computedAt;

    public ValuationResult() {
    }

    public ValuationResult(UUID resultId,
                           ValuationAssumption valuationAssumption,
                           BigDecimal netPresentValue,
                           BigDecimal internalRateReturn,
                           BigDecimal cashOnCash,
                           int payBackMonth,
                           String jsonCashFlows,
                           String jsonSensitivities,
                           Timestamp computedAt) {
        this.resultId = resultId;
        this.valuationAssumption = valuationAssumption;
        this.netPresentValue = netPresentValue;
        this.internalRateReturn = internalRateReturn;
        this.cashOnCash = cashOnCash;
        this.payBackMonth = payBackMonth;
        this.jsonCashFlows = jsonCashFlows;
        this.jsonSensitivities = jsonSensitivities;
        this.computedAt = computedAt;
    }

    public UUID getResultId() {
        return resultId;
    }

    public void setResultId(UUID resultId) {
        this.resultId = resultId;
    }

    public ValuationAssumption getValuationAssumption() {
        return valuationAssumption;
    }

    public void setValuationAssumption(ValuationAssumption valuationAssumption) {
        this.valuationAssumption = valuationAssumption;
    }

    public BigDecimal getNetPresentValue() {
        return netPresentValue;
    }

    public void setNetPresentValue(BigDecimal netPresentValue) {
        this.netPresentValue = netPresentValue;
    }

    public BigDecimal getInternalRateReturn() {
        return internalRateReturn;
    }

    public void setInternalRateReturn(BigDecimal internalRateReturn) {
        this.internalRateReturn = internalRateReturn;
    }

    public BigDecimal getCashOnCash() {
        return cashOnCash;
    }

    public void setCashOnCash(BigDecimal cashOnCash) {
        this.cashOnCash = cashOnCash;
    }

    public int getPayBackMonth() {
        return payBackMonth;
    }

    public void setPayBackMonth(int payBackMonth) {
        this.payBackMonth = payBackMonth;
    }

    public String getJsonCashFlows() {
        return jsonCashFlows;
    }

    public void setJsonCashFlows(String jsonCashFlows) {
        this.jsonCashFlows = jsonCashFlows;
    }

    public String getJsonSensitivities() {
        return jsonSensitivities;
    }

    public void setJsonSensitivities(String jsonSensitivities) {
        this.jsonSensitivities = jsonSensitivities;
    }

    public Timestamp getComputedAt() {
        return computedAt;
    }

    public void setComputedAt(Timestamp computedAt) {
        this.computedAt = computedAt;
    }
}

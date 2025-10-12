package com.arete.korbly.modules.valuation.domain;

import com.arete.korbly.modules.valuation.dto.CashFlowLineItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValuationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID valuationResultId;

    private UUID valuationAssumptionId;

    // key metrics
    private BigDecimal netPresentValue;            // NPV
    private Double internalRateOfReturnAnnual;     // IRR % annual (nullable)
    private Double cashOnCashMultiple;             // CoC (nullable)
    private Integer paybackPeriodInMonths;         // nullable

    // store schedule
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<CashFlowLineItem> cashflowSchedule;

    // store basic sensitivities
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<double[]> npvVsDiscountRate; // [rate, npv]

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<double[]> npvVsTenorMonths;  // [deltaMonths, npv]

    // audit
    private Timestamp computedAt;
    private UUID computedByUserId;
}

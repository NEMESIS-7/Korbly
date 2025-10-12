package com.arete.korbly.modules.valuation.domain;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.syndication.domain.Tranche;
import com.arete.korbly.modules.syndication.enums.DealCurrency;
import com.arete.korbly.modules.termsheet.enums.AmortizationStructure;
import com.arete.korbly.modules.valuation.enums.ValuationSource;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_valuation_tranche", columnList = "tranche_id"),
                @Index(name = "idx_valuation_asof", columnList = "as_of"),
                @Index(name = "idx_valuation_created_at", columnList = "createdAt")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValuationAssumption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID valuationId;

    /** Linkage */
    @ManyToOne
    @JoinColumn(name = "tranche_id", nullable = false)
    private Tranche tranche;

    /** Valuation timestamp (when these assumptions apply) */
    @Column(name = "as_of", nullable = false)
    private Timestamp asOf;

    /** Schedule start date (period t=0 occurs at this month’s start). */
    @Column(nullable = false)
    private LocalDate scheduleStartDate;

    /** Core economics (mirror CashflowAssumption 1:1) */
    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal principal;

    /** Annual nominal interest rate as decimal (e.g., 0.25 = 25%) */
    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal annualRate;

    @Column(nullable = false)
    private int tenorMonths;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AmortizationStructure amortizationStructure;

    /** Grace windows */
    @Column(nullable = false)
    private int gracePrincipalMonths = 0;

    @Column(nullable = false)
    private int graceInterestMonths = 0;

    /** Fees (percentages as decimals except servicingBps which is bps) */
    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal feeUpfrontPct = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal feeServicingBps = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal feeExitPct = BigDecimal.ZERO;

    /** Valuation-only input: investor discount rate (decimal, annual) */
    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal annualDiscountRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DealCurrency currency = DealCurrency.GHS;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ValuationSource source;

    /** Optional scenario label for UI/packaging (e.g., "Base", "Bear", "Bull") */
    @Column(length = 64)
    private String scenarioLabel;


    /** If provided, final balloon as % of original principal (e.g., 0.30 = 30%) */
    @Column(precision = 10, scale = 6)
    private BigDecimal balloonPercentOfOriginal;

    /** If provided, explicit balloon amount at maturity (overrides percent) */
    @Column(precision = 20, scale = 2)
    private BigDecimal balloonAmountAtMaturity;

    /** If provided, explicit fixed monthly payment for partial amortization */
    @Column(precision = 20, scale = 2)
    private BigDecimal fixedMonthlyPayment;


    /** Number of months negative amortization is allowed */
    private Integer negativeAmortizationMonths;

    /** Minimum payment as % of interest (e.g., 0.50 = pay at least 50% of interest) */
    @Column(precision = 10, scale = 6)
    private BigDecimal minPaymentPercentOfInterest;

    /** Minimum absolute payment amount (alternative to % of interest) */
    @Column(precision = 20, scale = 2)
    private BigDecimal minPaymentAbsoluteAmount;

    /** Cap multiple on principal during neg-am (e.g., 1.25 = 125% of original) */
    @Column(precision = 10, scale = 6)
    private BigDecimal negativeAmortizationCapMultiple;

    /** Audit */
    @OneToOne
    @JoinColumn(name = "created_by", nullable = false)
    private AppUser createdBy;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeleteYn deleteYn = DeleteYn.N;
}
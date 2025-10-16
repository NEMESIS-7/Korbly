package com.arete.korbly.modules.sme.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_sme_id", columnList = "sme_id"),
                @Index(name = "idx_period_months", columnList = "period_month")
        }
)
public class SmeMonthlyFinancials {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID financialId;

    @ManyToOne
    @JoinColumn(name = "sme_id")
    private SME sme;

    private LocalDate periodMonth;

    private BigDecimal revenue;

    private BigDecimal operatingCashFlow;


}

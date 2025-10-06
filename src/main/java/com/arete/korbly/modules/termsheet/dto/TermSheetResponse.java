package com.arete.korbly.modules.termsheet.dto;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.syndication.enums.TrancheType;
import com.arete.korbly.modules.termsheet.enums.AmortizationStructure;
import com.arete.korbly.modules.termsheet.enums.GoverningLaw;
import com.arete.korbly.modules.termsheet.enums.TermSheetStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TermSheetResponse {

    private UUID termSheetId;
    private UUID dealId;
    private UUID trancheId;
    private UUID smeId;

    private BigDecimal loanAmount;
    private Double interestRate;
    private LocalDate maturityDate;

    private AmortizationStructure amortizationStructure;
    private Boolean prepaymentOption;

    private Map<String, LocalDate> offeringPeriod;
    private Map<String, List<String>> guarantees;
    private Map<String, List<String>> collateral;

    private TrancheType seniority;
    private Map<String, List<String>> covenants;
    private Map<String, List<String>> eventsOfDefault;

    private Double defaultRate;
    private Map<String, String> gracePeriods;
    private GoverningLaw governingLaw;

    private TermSheetStatus sheetStatus;
    private Integer sheetVersion;
    private Boolean isLatest;

    private UUID parentId;

    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp signedAt;

    private AppUser createdBy;
}

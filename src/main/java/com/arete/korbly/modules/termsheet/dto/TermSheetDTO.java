package com.arete.korbly.modules.termsheet.dto;
import com.arete.korbly.modules.syndication.enums.TrancheType;
import com.arete.korbly.modules.termsheet.enums.*;
import com.arete.korbly.modules.shared.enums.DeleteYn;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record TermSheetDTO(
        UUID dealId,
        UUID trancheId,
        UUID smeId,
        BigDecimal loanAmount,
        Double interestRate,
        LocalDate maturityDate,
        AmortizationStructure amortizationStructure,
        Boolean prepaymentOption,

        Map<String, LocalDate> offeringPeriod,
        Map<String, List<String>> guarantees,
        Map<String, List<String>> collateral,
        TrancheType seniority,
        Map<String, List<String>> covenants,
        Map<String, List<String>> eventsOfDefault,
        Double defaultRate,
        Map<String, String> gracePeriods,
        GoverningLaw governingLaw,

        TermSheetStatus sheetStatus,
        Integer sheetVersion,
        UUID parentId,
        Boolean isLatest,

        Timestamp createdAt,
        Timestamp updatedAt,
        Timestamp signedAt,
        UUID createdBy,

        DeleteYn deleteYn,

        List<ConditionPrecedentDTO> conditionsPrecedent
) {}

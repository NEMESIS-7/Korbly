package com.arete.korbly.modules.termsheet.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TermSheetPdfDTO(
        UUID termSheetId,
        String dealTitle,
        String smeName,
        UUID trancheId,
        BigDecimal loanAmount,
        Double interestRate,
        LocalDate maturityDate,
        String amortization,
        Boolean prepaymentOption,
        String seniority,
        String governingLaw,
        Integer version,
        LocalDate createdDate,
        List<CpPdfDTO> cps
) {}
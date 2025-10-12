package com.arete.korbly.modules.valuation.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ValuationPreviewResponse(
        List<CashflowRow> schedule,
        BigDecimal npv,
        Double irrAnnual,
        Double cashOnCash,
        Integer paybackMonth,
        Map<String, Object> sensitivities,
        List<String> warnings
) {}

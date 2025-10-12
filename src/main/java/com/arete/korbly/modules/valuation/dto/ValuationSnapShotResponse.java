package com.arete.korbly.modules.valuation.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ValuationSnapShotResponse(
        UUID valuationId,
        UUID trancheId,
        Instant computedAt,
        BigDecimal npv,
        Double irrAnnual,
        Double cashOnCash,
        Integer paybackMonth
) {}
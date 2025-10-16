package com.arete.korbly.modules.sme.dto;

import java.math.BigDecimal;

public record SfmSeriesPoint(
        String month,                      // "YYYY-MM"
        BigDecimal revenue,
        BigDecimal operatingCashflow
) {}
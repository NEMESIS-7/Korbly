package com.arete.korbly.modules.sme.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public record SfmSeriesResponse(
        UUID smeId,
        Timestamp asOf,
        List<SfmSeriesPoint> points,
        BigDecimal avgRevenue,         // over window returned
        BigDecimal avgOperatingCashflow
) {}
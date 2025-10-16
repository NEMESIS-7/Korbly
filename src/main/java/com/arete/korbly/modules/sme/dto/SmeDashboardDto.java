package com.arete.korbly.modules.sme.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public record SmeDashboardDto(
        UUID smeId,
        Timestamp asOf,
        List<RevenuePoint> monthlyRevenue,
        int openApplications,
        BigDecimal averageMonthlyOperatingCashflow,
        CreditHealthDTO creditHealth
) {}


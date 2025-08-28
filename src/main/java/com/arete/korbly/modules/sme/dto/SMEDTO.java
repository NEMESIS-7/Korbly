package com.arete.korbly.modules.sme.dto;

import com.arete.korbly.modules.shared.enums.SMEIndustry;
import com.arete.korbly.modules.shared.enums.SMERegion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SMEDTO(
        UUID smeId,
        String companyName,
        SMEIndustry industry,
        String registrationNumber,
        String phoneNumber,
        SMERegion region,
        LocalDate dateEstablished,
        String websiteURL,
        String businessDescription,
        BigDecimal annualRevenue,
        Integer numberOfEmployees,
        BigDecimal monthlyRevenue,
        BigDecimal requestedAmount,
        String purposeOfFunding,
        String certOfIncorporation,
        String latestFinancialStatements,
        String businessPlan,
        String taxClearanceCert
) {
}

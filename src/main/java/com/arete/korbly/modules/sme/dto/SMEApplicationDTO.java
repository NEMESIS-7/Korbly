package com.arete.korbly.modules.sme.dto;

import com.arete.korbly.modules.shared.enums.SMEIndustry;
import com.arete.korbly.modules.shared.enums.SMERegion;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SMEApplicationDTO(
        String companyName,
        SMEIndustry industry,
        String registrationNumber,
        String primaryContactEmail,
        String phoneNumber,
        SMERegion region,
        LocalDate dateEstablished,
        String websiteURL,
        String businessDescription,
        BigDecimal annualRevenue,
        Integer numberOfEmployees,
        BigDecimal monthlyRevenue,
        BigDecimal requestedAmount,
        String purposeOfFunding
) {
}

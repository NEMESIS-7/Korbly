package com.arete.korbly.modules.regulator.dto;

import com.arete.korbly.modules.shared.enums.SMEIndustry;
import com.arete.korbly.modules.syndication.enums.DealCurrency;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record RegulatorDealViewDTO(
        UUID dealId,
        String dealTitle,
        String dealDescription,
        SMEIndustry dealSector,
        DealCurrency dealCurrency,
        BigDecimal totalAmount,
        List<RegulatorTrancheViewDTO> tranches
) {
}

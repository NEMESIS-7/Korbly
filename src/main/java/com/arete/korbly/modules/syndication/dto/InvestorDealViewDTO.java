package com.arete.korbly.modules.syndication.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record InvestorDealViewDTO(
        UUID dealId,
        String dealTitle,
        String dealDescription,
        String dealSector,
        String currency,
        BigDecimal totalAmount,
        List<InvestorTrancheViewDTO> tranches
) {}

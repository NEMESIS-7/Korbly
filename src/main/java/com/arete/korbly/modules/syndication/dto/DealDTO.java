package com.arete.korbly.modules.syndication.dto;

import com.arete.korbly.modules.shared.enums.SMEIndustry;
import com.arete.korbly.modules.syndication.enums.DealCurrency;
import com.arete.korbly.modules.syndication.enums.DealStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record DealDTO(

        UUID dealId,

        @NotBlank
        @Size(max = 150)
        String dealTitle,

        String dealDescription,

        DealStatus dealStatus,

        DealCurrency currency,
        List<TrancheDTO> tranches,

        @NotNull
        @DecimalMin("0.0")
        BigDecimal totalAmount,

        SMEIndustry dealSector
) {
}

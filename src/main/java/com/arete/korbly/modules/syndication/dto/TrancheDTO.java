package com.arete.korbly.modules.syndication.dto;

import com.arete.korbly.modules.syndication.enums.TrancheType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record TrancheDTO(
        @NotNull
        TrancheType trancheType,

        @NotNull
        @DecimalMin("0.0")
        BigDecimal amount,

        @NotNull
        @DecimalMin("0.0")
        BigDecimal interestRate,

        @NotNull
        @Min(1)
        Integer tenorMonths,

        Boolean isAnchor,

        Timestamp createdAt,
        Timestamp updatedAt
) {}
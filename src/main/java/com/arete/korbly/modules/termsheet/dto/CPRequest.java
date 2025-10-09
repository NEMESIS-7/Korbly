package com.arete.korbly.modules.termsheet.dto;

import com.arete.korbly.modules.termsheet.enums.CPCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CPRequest(
        @NotNull CPCode cpCode,
        @NotBlank String title,
        @NotBlank String description,
        @NotNull Boolean required
) {}

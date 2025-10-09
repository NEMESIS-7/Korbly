package com.arete.korbly.modules.termsheet.dto;

import java.util.UUID;

public record CpPdfDTO(
        UUID cpId,
        String title,
        String description,
        boolean required,
        String status,
        String evidenceFileKey,
        String waiverReason
) {}
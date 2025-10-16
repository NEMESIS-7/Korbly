package com.arete.korbly.modules.sme.dto;

import java.math.BigDecimal;

public record CreditHealthDTO(
        Double dscr,
        Double icr,
        BigDecimal altmanZ,
        BigDecimal ohlsonO,
        String label            // "Healthy" | "Moderate" | "Weak"
) {}

package com.arete.korbly.modules.valuation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CashflowRow(
        int monthIndex,
        LocalDate date,
        BigDecimal opening,
        BigDecimal interest,
        BigDecimal principal,
        BigDecimal feesNet,
        BigDecimal total,
        BigDecimal closing
) {}

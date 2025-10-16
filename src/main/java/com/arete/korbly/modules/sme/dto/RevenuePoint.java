package com.arete.korbly.modules.sme.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record RevenuePoint(
        LocalDate periodMonth,
        BigDecimal revenue
) {
    public String month() {
        return periodMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    public BigDecimal revenue() {
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
}

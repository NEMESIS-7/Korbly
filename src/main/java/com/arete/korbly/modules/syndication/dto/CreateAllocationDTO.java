package com.arete.korbly.modules.syndication.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateAllocationDTO(
        UUID trancheId,
        UUID investorId,
        BigDecimal amount
) {

}

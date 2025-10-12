package com.arete.korbly.modules.valuation.dto;

import java.util.UUID;

public record ValuationSummaryResponse (
        UUID valuationAssumptionId,
        UUID valuationResultId,
        String scenarioLabel
){
}

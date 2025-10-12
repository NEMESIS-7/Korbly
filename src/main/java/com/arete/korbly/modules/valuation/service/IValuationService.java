package com.arete.korbly.modules.valuation.service;

import com.arete.korbly.modules.valuation.dto.ValuationPreviewRequest;
import com.arete.korbly.modules.valuation.dto.ValuationPreviewResponse;
import com.arete.korbly.modules.valuation.dto.ValuationSummaryResponse;

import java.util.UUID;

public interface IValuationService {
    ValuationPreviewResponse preview(ValuationPreviewRequest request, UUID requestedByUserId);
    ValuationSummaryResponse commit(UUID valuationAssumptionId, UUID committedByUserId);
    ValuationPreviewResponse getLatestByAssumption(UUID valuationAssumptionId);
}

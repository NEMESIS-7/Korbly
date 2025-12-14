package com.arete.korbly.modules.valuation.web;

import com.arete.korbly.modules.shared.GetUser;
import com.arete.korbly.modules.valuation.dto.ValuationPreviewRequest;
import com.arete.korbly.modules.valuation.dto.ValuationPreviewResponse;
import com.arete.korbly.modules.valuation.dto.ValuationSummaryResponse;
import com.arete.korbly.modules.valuation.service.ValuationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/valuation")
public class ValuationController {

    private final ValuationService valuationService;
    private final GetUser getUser;

    public ValuationController(ValuationService valuationService, GetUser getUser) {
        this.valuationService = valuationService;
        this.getUser = getUser;
    }

    @PostMapping("/preview")
    public ResponseEntity<ValuationPreviewResponse> preview(
            @Valid @RequestBody ValuationPreviewRequest request) {
        UUID appUserId = getUser.getCurrentAuthenticatedUserId();
        return new ResponseEntity<>(
                valuationService.preview(request, appUserId), HttpStatus.OK
        );
    }

    @PostMapping("/commit/{assumptionId}")
    public ResponseEntity<ValuationSummaryResponse> commit(
            @PathVariable UUID assumptionId) {
        UUID appUserId = getUser.getCurrentAuthenticatedUserId();
        return ResponseEntity.ok(
                valuationService.commit(assumptionId, appUserId)
        );
    }

    @GetMapping("/assumptions/{assumptionId}/latest")
    public ResponseEntity<ValuationPreviewResponse> latest(
            @PathVariable UUID assumptionId) {
        return ResponseEntity.ok(
                valuationService.getLatestByAssumption(assumptionId)
        );
    }
}


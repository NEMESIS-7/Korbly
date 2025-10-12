package com.arete.korbly.modules.valuation.web;

import com.arete.korbly.infrastructure.security.JWTService;
import com.arete.korbly.modules.valuation.dto.ValuationPreviewRequest;
import com.arete.korbly.modules.valuation.dto.ValuationPreviewResponse;
import com.arete.korbly.modules.valuation.dto.ValuationSummaryResponse;
import com.arete.korbly.modules.valuation.service.ValuationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/valuation")
public class ValuationController {

    private final ValuationService valuationService;
    private final JWTService jwtService;
    private final HttpServletRequest httpServletRequest;

    public ValuationController(ValuationService valuationService,
                               JWTService jwtService,
                               HttpServletRequest httpServletRequest) {
        this.valuationService = valuationService;
        this.jwtService = jwtService;
        this.httpServletRequest = httpServletRequest;
    }

    @PostMapping("/preview")
    public ResponseEntity<ValuationPreviewResponse> preview(
            @Valid @RequestBody ValuationPreviewRequest request) {
        UUID appUserId = jwtService.extractAppUserId(httpServletRequest);
        return new ResponseEntity<>(
                valuationService.preview(request, appUserId), HttpStatus.OK
        );
    }

    @PostMapping("/commit/{assumptionId}")
    public ResponseEntity<ValuationSummaryResponse> commit(
            @PathVariable UUID assumptionId) {
        UUID appUserId = jwtService.extractAppUserId(httpServletRequest);
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


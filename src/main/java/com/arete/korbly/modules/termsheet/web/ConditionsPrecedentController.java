package com.arete.korbly.modules.termsheet.web;

import com.arete.korbly.modules.termsheet.dto.CPRequest;
import com.arete.korbly.modules.termsheet.dto.CPResponse;
import com.arete.korbly.modules.termsheet.dto.ConditionPrecedentDTO;
import com.arete.korbly.modules.termsheet.enums.CPStatus;
import com.arete.korbly.modules.termsheet.service.IConditionsPrecedentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/conditions-precedent")
public class ConditionsPrecedentController {

    private final IConditionsPrecedentService conditionsPrecedentService;

    public ConditionsPrecedentController(IConditionsPrecedentService conditionsPrecedentService) {
        this.conditionsPrecedentService = conditionsPrecedentService;
    }

    /**
     * Add a new condition precedent to a term sheet
     * POST /api/v1/conditions-precedent/sheet/{sheetId}
     */
    @PostMapping("/sheet/{sheetId}")
    public ResponseEntity<CPResponse> addCondition(
            @PathVariable UUID sheetId,
            @Valid @RequestBody CPRequest request,
            HttpServletRequest httpRequest) {

        CPResponse response = conditionsPrecedentService.addCondition(sheetId, request, httpRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update an existing condition precedent
     * PUT /api/v1/conditions-precedent/{cpId}
     */
    @PutMapping("/{cpId}")
    public ResponseEntity<CPResponse> updateCondition(
            @PathVariable UUID cpId,
            @Valid @RequestBody ConditionPrecedentDTO dto,
            HttpServletRequest httpRequest) {

        CPResponse response = conditionsPrecedentService.updateCondition(cpId, dto, httpRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update the status of a condition precedent (PENDING -> APPROVED/REJECTED)
     * PATCH /api/v1/conditions-precedent/{cpId}/status
     */
    @PatchMapping("/{cpId}/status")
    public ResponseEntity<Map<String, String>> updateStatus(
            @PathVariable UUID cpId,
            @RequestParam CPStatus status,
            @RequestParam UUID approvedByUserId,
            HttpServletRequest httpRequest) {

        conditionsPrecedentService.updateStatus(cpId, status, approvedByUserId, httpRequest);
        return new ResponseEntity<>(Map.of("message", "Status updated successfully"), HttpStatus.OK);
    }

    /**
     * Get a specific condition precedent by ID
     * GET /api/v1/conditions-precedent/{cpId}
     */
    @GetMapping("/{cpId}")
    public ResponseEntity<CPResponse> getCondition(
            @PathVariable UUID cpId,
            HttpServletRequest httpRequest) {

        CPResponse response = conditionsPrecedentService.getCondition(cpId, httpRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all conditions precedent for a term sheet
     * GET /api/v1/conditions-precedent/sheet/{sheetId}
     */
    @GetMapping("/sheet/{sheetId}")
    public ResponseEntity<List<CPResponse>> getConditionsForSheet(
            @PathVariable UUID sheetId,
            HttpServletRequest httpRequest) {

        List<CPResponse> responses = conditionsPrecedentService.getConditionsForSheet(sheetId, httpRequest);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    /**
     * Waive a condition precedent
     * PATCH /api/v1/conditions-precedent/{cpId}/waive
     */
    @PatchMapping("/{cpId}/waive")
    public ResponseEntity<Map<String, String>> waiveCondition(
            @PathVariable UUID cpId,
            @RequestParam String waiverReason,
            @RequestParam UUID approvedByUserId,
            HttpServletRequest httpRequest) {

        conditionsPrecedentService.waiveCondition(cpId, waiverReason, approvedByUserId, httpRequest);
        return new ResponseEntity<>(Map.of("message", "Condition waived successfully"), HttpStatus.OK);
    }

    /**
     * Attach evidence to a condition precedent
     * PATCH /api/v1/conditions-precedent/{cpId}/evidence
     */
    @PatchMapping("/{cpId}/evidence")
    public ResponseEntity<Map<String, String>> attachEvidence(
            @PathVariable UUID cpId,
            @RequestParam String evidenceFileKey,
            HttpServletRequest httpRequest) {

        conditionsPrecedentService.attachEvidence(cpId, evidenceFileKey, httpRequest);
        return new ResponseEntity<>(Map.of("message", "Evidence attached successfully"), HttpStatus.OK);
    }

    /**
     * Soft delete a condition precedent
     * DELETE /api/v1/conditions-precedent/{cpId}
     */
    @DeleteMapping("/{cpId}")
    public ResponseEntity<Map<String, String>> deleteCondition(
            @PathVariable UUID cpId,
            HttpServletRequest httpRequest) {

        conditionsPrecedentService.markAsDeleted(cpId, httpRequest);
        return new ResponseEntity<>(Map.of("message", "Condition deleted successfully"), HttpStatus.OK);
    }
}

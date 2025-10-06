package com.arete.korbly.modules.termsheet.web;

import com.arete.korbly.infrastructure.security.JWTService;
import com.arete.korbly.modules.termsheet.dto.TermSheetDTO;
import com.arete.korbly.modules.termsheet.dto.TermSheetResponse;
import com.arete.korbly.modules.termsheet.service.TermSheetService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/termsheets")
public class TermSheetController {

    private final TermSheetService termSheetService;
    private final JWTService jwtService;

    public TermSheetController(TermSheetService termSheetService,
                               JWTService jwtService) {
        this.termSheetService = termSheetService;
        this.jwtService = jwtService;
    }

    private UUID getAppUserId(HttpServletRequest request) {
        UUID appUserId = jwtService.extractAppUserId(request);
        System.out.println("app user ID: " + appUserId);
        return appUserId;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TermSheetDTO dto, HttpServletRequest request) {
        UUID createdByUserId = getAppUserId(request);
        return new ResponseEntity<>(termSheetService.createTermSheet(dto, createdByUserId), HttpStatus.OK);
    }

    @PostMapping("/{parentId}/amend")
    public ResponseEntity<?> amend(
            @PathVariable UUID parentId,
            @RequestBody TermSheetDTO dto,
            HttpServletRequest request
    ) {
        UUID amendedBy = getAppUserId(request);
        return new ResponseEntity<>(termSheetService.amendTermSheet(parentId, dto, amendedBy), HttpStatus.OK);
    }

    @GetMapping("/{termSheetId}")
    public ResponseEntity<?> getById(@PathVariable UUID termSheetId) {
        return new ResponseEntity<>(termSheetService.getTermSheetById(termSheetId), HttpStatus.OK);
    }

    @GetMapping("/{parentId}/versions")
    public ResponseEntity<?> getAllVersions(@PathVariable UUID parentId, Pageable pageable) {
        return new ResponseEntity<>(termSheetService.getAllVersions(parentId, pageable), HttpStatus.OK);
    }

    @GetMapping("/{parentId}/latest")
    public ResponseEntity<TermSheetResponse> getLatest(@PathVariable UUID parentId) {
        return new ResponseEntity<>(termSheetService.getLatestVersion(parentId), HttpStatus.OK);
    }

    @GetMapping("/deal/{dealId}")
    public ResponseEntity<?> getByDeal(@PathVariable UUID dealId, Pageable pageable) {
        return new ResponseEntity<>(termSheetService.findByDeal(dealId, pageable), HttpStatus.OK);
    }

    @GetMapping("/tranche/{trancheId}")
    public ResponseEntity<?> getByTranche(@PathVariable UUID trancheId, Pageable pageable) {
        return new ResponseEntity<>(termSheetService.findByTranche(trancheId, pageable), HttpStatus.OK);
    }

    @GetMapping("/sme/{smeId}")
    public ResponseEntity<?> getBySME(@PathVariable UUID smeId, Pageable pageable) {
        return new ResponseEntity<>(termSheetService.findBySME(smeId, pageable), HttpStatus.OK);
    }

    @PutMapping("/{termSheetId}")
    public ResponseEntity<TermSheetResponse> update(
            @PathVariable UUID termSheetId,
            @RequestBody TermSheetDTO dto
    ) {
        return new ResponseEntity<>(termSheetService.updateTermSheet(termSheetId, dto), HttpStatus.OK);
    }


    @PostMapping("/{termSheetId}/sign")
    public ResponseEntity<?> sign(
            @PathVariable UUID termSheetId,
            HttpServletRequest request
    ) {

        UUID signedByUserId = getAppUserId(request);
        termSheetService.signTermSheet(termSheetId, signedByUserId);
        return new ResponseEntity<>("Termsheet signed successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{termSheetId}/delete")
    public ResponseEntity<Void> markDeleted(@PathVariable UUID termSheetId) {
        termSheetService.markAsDeleted(termSheetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{termSheetId}/latest")
    public ResponseEntity<Void> markAsLatest(@PathVariable UUID termSheetId) {
        termSheetService.markAsLatest(termSheetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

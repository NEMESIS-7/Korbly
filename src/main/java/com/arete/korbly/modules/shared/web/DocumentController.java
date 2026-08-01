package com.arete.korbly.modules.shared.web;

import com.arete.korbly.modules.investor.persistence.InvestorRepository;
import com.arete.korbly.modules.shared.GetUser;
import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.UserType;
import com.arete.korbly.modules.shared.exceptions.UserNotFound;
import com.arete.korbly.modules.shared.service.AuthService;
import com.arete.korbly.modules.sme.persistence.SMERepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final AuthService authService;
    private final GetUser getUser;
    private final InvestorRepository investorRepository;
    private final SMERepository smeRepository;

    public DocumentController(AuthService authService, GetUser getUser,
                              InvestorRepository investorRepository, SMERepository smeRepository) {
        this.authService = authService;
        this.getUser = getUser;
        this.investorRepository = investorRepository;
        this.smeRepository = smeRepository;
    }

    @GetMapping("/investor/{investorId}/all")
    public ResponseEntity<?> getAllInvestorDocuments(
            @PathVariable UUID investorId,
            @RequestParam(defaultValue = "15") int expirationMinutes) {
        checkInvestorAccess(investorId);
        return new ResponseEntity<>(authService.getInvestorDocumentUrls(investorId, expirationMinutes), HttpStatus.OK);
    }

    @GetMapping("/sme/{smeId}/all")
    public ResponseEntity<?> getAllSMEDocuments(
            @PathVariable UUID smeId,
            @RequestParam(defaultValue = "15") int expirationMinutes) {
        checkSMEAccess(smeId);
        return new ResponseEntity<>(authService.getSMEDocumentUrls(smeId, expirationMinutes), HttpStatus.OK);
    }

    @GetMapping("/investor/{investorId}/document/{documentType}")
    public ResponseEntity<?> getInvestorDocument(
            @PathVariable UUID investorId,
            @PathVariable String documentType,
            @RequestParam(defaultValue = "15") int expirationMinutes) {
        try {
            checkInvestorAccess(investorId);
            String documentUrl = authService.getInvestorDocumentByType(investorId, documentType, expirationMinutes);
            return ResponseEntity.ok(Map.of("url", documentUrl, "documentType", documentType));
        } catch (UserNotFound e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/sme/{smeId}/document/{documentType}")
    public ResponseEntity<?> getSMEDocument(
            @PathVariable UUID smeId,
            @PathVariable String documentType,
            @RequestParam(defaultValue = "15") int expirationMinutes) {
        try {
            checkSMEAccess(smeId);
            String documentUrl = authService.getSMEDocumentByType(smeId, documentType, expirationMinutes);
            return ResponseEntity.ok(Map.of("url", documentUrl, "documentType", documentType));
        } catch (UserNotFound e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private void checkInvestorAccess(UUID investorId) {
        AppUser currentUser = getUser.getCurrentAuthenticatedUser();
        if (isPrivilegedUser(currentUser)) return;

        UUID ownerAppUserId = investorRepository.findByInvestorId(investorId)
                .map(inv -> inv.getAppUser().getUserId())
                .orElseThrow(() -> new UserNotFound("Investor not found"));

        if (!ownerAppUserId.equals(currentUser.getUserId())) {
            throw new SecurityException("Access denied: you do not own this investor account.");
        }
    }

    private void checkSMEAccess(UUID smeId) {
        AppUser currentUser = getUser.getCurrentAuthenticatedUser();
        if (isPrivilegedUser(currentUser)) return;

        UUID ownerAppUserId = smeRepository.findById(smeId)
                .map(sme -> sme.getAppUser().getUserId())
                .orElseThrow(() -> new UserNotFound("SME not found"));

        if (!ownerAppUserId.equals(currentUser.getUserId())) {
            throw new SecurityException("Access denied: you do not own this SME account.");
        }
    }

    private boolean isPrivilegedUser(AppUser user) {
        return user.getUserType() == UserType.ADMIN
                || user.getUserType() == UserType.REGULATORY_AUTHORITY;
    }
}

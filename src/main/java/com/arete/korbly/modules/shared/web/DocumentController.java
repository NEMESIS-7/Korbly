package com.arete.korbly.modules.shared.web;

import com.arete.korbly.modules.shared.exceptions.UserNotFound;
import com.arete.korbly.modules.shared.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final AuthService authService;

    public DocumentController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/investor/{investorId}/all")
    public ResponseEntity<?> getAllInvestorDocuments(
            @PathVariable UUID investorId,
            @RequestParam(defaultValue = "15") int expirationMinutes) {
        return new ResponseEntity<>( authService.getInvestorDocumentUrls(investorId, expirationMinutes), HttpStatus.OK);
    }

    @GetMapping("/sme/{smeId}/all")
    public ResponseEntity<?> getAllSMEDocuments(
            @PathVariable UUID smeId,
            @RequestParam(defaultValue = "15") int expirationMinutes) {
        return new ResponseEntity<>(authService.getSMEDocumentUrls(smeId, expirationMinutes), HttpStatus.OK);
    }

    @GetMapping("/investor/{investorId}/document/{documentType}")
    public ResponseEntity<?> getInvestorDocument(
            @PathVariable UUID investorId,
            @PathVariable String documentType,
            @RequestParam(defaultValue = "15") int expirationMinutes) {

        try {
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
}

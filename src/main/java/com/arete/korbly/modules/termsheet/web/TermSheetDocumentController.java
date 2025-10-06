package com.arete.korbly.modules.termsheet.web;

import com.arete.korbly.modules.termsheet.service.ITermSheetDocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/termsheets")
public class TermSheetDocumentController {

    private final ITermSheetDocumentService documentService;

    public TermSheetDocumentController(ITermSheetDocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Generate and store the PDF in S3 for a term sheet.
     */
    @PostMapping("/{termSheetId}/pdf")
    public ResponseEntity<String> generateAndStorePdf(@PathVariable UUID termSheetId) {
        String fileKey = documentService.generateAndStore(termSheetId);
        return ResponseEntity.ok(fileKey); // returns S3 key
    }

/*    *//**
     * Get a presigned URL for downloading the PDF.
     *//*
    @GetMapping("/{termSheetId}/pdf")
    public ResponseEntity<String> getPdfDownloadUrl(@PathVariable UUID termSheetId) {
        String url = documentService.getDownloadUrl(termSheetId);
        return ResponseEntity.ok(url);
    }*/
}

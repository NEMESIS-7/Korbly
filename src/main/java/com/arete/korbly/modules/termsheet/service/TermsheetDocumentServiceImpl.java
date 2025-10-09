package com.arete.korbly.modules.termsheet.service;

import com.arete.korbly.infrastructure.integrations.S3FileUploadService;
import com.arete.korbly.modules.shared.service.AuthService;
import com.arete.korbly.modules.termsheet.util.PDFGeneratorUtil;
import com.arete.korbly.modules.termsheet.domain.ConditionsPrecedent;
import com.arete.korbly.modules.termsheet.domain.TermSheet;
import com.arete.korbly.modules.termsheet.exceptions.TermSheetNotFound;
import com.arete.korbly.modules.termsheet.persistence.ConditionsPrecedentRepository;
import com.arete.korbly.modules.termsheet.persistence.TermSheetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TermsheetDocumentServiceImpl implements ITermSheetDocumentService {

    private final TermSheetRepository termSheetRepository;
    private final ConditionsPrecedentRepository cpRepository;
    private final S3FileUploadService s3StorageService;
    private final PDFGeneratorUtil pdfGenerator;
    private final AuthService authService;

    public TermsheetDocumentServiceImpl(
            TermSheetRepository termSheetRepository,
            ConditionsPrecedentRepository cpRepository,
            S3FileUploadService s3StorageService,
            PDFGeneratorUtil pdfGenerator,
            AuthService authService
    ) {
        this.termSheetRepository = termSheetRepository;
        this.cpRepository = cpRepository;
        this.s3StorageService = s3StorageService;
        this.pdfGenerator = pdfGenerator;
        this.authService = authService;
    }

    @Override
    public byte[] generatePDF(TermSheet termSheet, List<ConditionsPrecedent> cps) {
        return pdfGenerator.buildTermSheetPDF(termSheet, cps);
    }

    @Override
    public String uploadToS3(String smeName, int version, byte[] pdfBytes) {
        String key = "termsheets/" + smeName + "/" + version + "/TermSheet.pdf";
        return s3StorageService.uploadFile(key, pdfBytes);
    }

    @Override
    public String generateDownloadUrl(String fileKey) {
        return authService.generatePresignedDownloadUrl(fileKey, 10);
    }

    @Override
    public String generateAndStore(UUID termSheetId) {
        TermSheet sheet = termSheetRepository.findById(termSheetId)
                .orElseThrow(() -> new TermSheetNotFound("Term sheet not found"));
        List<ConditionsPrecedent> cps = cpRepository.findBySheetId(termSheetId);

        byte[] pdf = generatePDF(sheet, cps);
        String fileKey = uploadToS3(sheet.getSmeId().getCompanyName(), sheet.getSheetVersion(), pdf);

        termSheetRepository.save(sheet);

        return fileKey;
    }
}

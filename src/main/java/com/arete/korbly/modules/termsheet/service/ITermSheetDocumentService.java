package com.arete.korbly.modules.termsheet.service;

import com.arete.korbly.modules.termsheet.domain.ConditionsPrecedent;
import com.arete.korbly.modules.termsheet.domain.TermSheet;

import java.util.List;
import java.util.UUID;

public interface ITermSheetDocumentService {
    byte[] generatePDF(TermSheet termSheet, List<ConditionsPrecedent> cps);
    String uploadToS3(String smeName, int version, byte[] pdfBytes);
    String generateDownloadUrl(String fileKey);
    String generateAndStore(UUID termSheetId);
}

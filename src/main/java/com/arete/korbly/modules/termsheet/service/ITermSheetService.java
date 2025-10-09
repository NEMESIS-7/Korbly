package com.arete.korbly.modules.termsheet.service;

import com.arete.korbly.modules.termsheet.dto.TermSheetDTO;
import com.arete.korbly.modules.termsheet.dto.TermSheetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ITermSheetService {

    TermSheetResponse createTermSheet(TermSheetDTO dto, UUID createdByUserId);
    TermSheetResponse amendTermSheet(UUID parentId, TermSheetDTO dto, UUID amendedBy);


    TermSheetResponse getTermSheetById(UUID termSheetId);
    Page<TermSheetResponse> getAllVersions(UUID parentId, Pageable pageable);       // fetch version history
    TermSheetResponse getLatestVersion(UUID parentId);           // current “active” sheet


    TermSheetResponse updateTermSheet(UUID termSheetId, TermSheetDTO dto);
    void signTermSheet(UUID termSheetId, UUID signedByUserId);


    void markAsDeleted(UUID termSheetId);
    void markAsLatest(UUID termSheetId); // ensure only one version is latest


    Page<TermSheetResponse> findByDeal(UUID dealId, Pageable pageable);
    Page<TermSheetResponse> findByTranche(UUID trancheId, Pageable pageable);
    Page<TermSheetResponse> findBySME(UUID smeId, Pageable pageable);
}

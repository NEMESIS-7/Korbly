package com.arete.korbly.modules.termsheet.service;

import com.arete.korbly.modules.termsheet.dto.TermSheetDTO;
import com.arete.korbly.modules.termsheet.dto.TermSheetResponse;

import java.util.List;
import java.util.UUID;

public interface ITermSheetService {

    TermSheetResponse createTermSheet(TermSheetDTO dto, UUID createdByUserId);
    TermSheetResponse amendTermSheet(UUID parentId, TermSheetDTO dto, UUID createdByUserId);


    TermSheetResponse getTermSheetById(UUID termSheetId);
    List<TermSheetResponse> getAllVersions(UUID parentId);       // fetch version history
    TermSheetResponse getLatestVersion(UUID parentId);           // current “active” sheet


    TermSheetResponse updateTermSheet(UUID termSheetId, TermSheetDTO dto);
    void signTermSheet(UUID termSheetId, UUID signedByUserId);


    void markAsDeleted(UUID termSheetId);
    void markAsLatest(UUID termSheetId); // ensure only one version is latest


    List<TermSheetResponse> findByDeal(UUID dealId);
    List<TermSheetResponse> findByTranche(UUID trancheId);
    List<TermSheetResponse> findBySME(UUID smeId);
}

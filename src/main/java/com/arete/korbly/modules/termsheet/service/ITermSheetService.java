package com.arete.korbly.modules.termsheet.service;

import com.arete.korbly.modules.termsheet.domain.TermSheet;
import com.arete.korbly.modules.termsheet.dto.TermSheetDTO;

import java.util.List;
import java.util.UUID;

public interface ITermSheetService {

    TermSheet createTermSheet(TermSheetDTO dto, UUID createdByUserId);
    TermSheet amendTermSheet(UUID parentId, TermSheetDTO dto, UUID createdByUserId);


    TermSheet getTermSheetById(UUID termSheetId);
    List<TermSheet> getAllVersions(UUID parentId);       // fetch version history
    TermSheet getLatestVersion(UUID parentId);           // current “active” sheet


    TermSheet updateTermSheet(UUID termSheetId, TermSheetDTO dto);
    void signTermSheet(UUID termSheetId, UUID signedByUserId);


    void markAsDeleted(UUID termSheetId);
    void markAsLatest(UUID termSheetId); // ensure only one version is latest


    List<TermSheet> findByDeal(UUID dealId);
    List<TermSheet> findByTranche(UUID trancheId);
    List<TermSheet> findBySME(UUID smeId);
}

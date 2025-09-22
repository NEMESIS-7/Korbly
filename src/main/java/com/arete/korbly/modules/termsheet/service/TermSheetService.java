package com.arete.korbly.modules.termsheet.service;

import com.arete.korbly.modules.termsheet.domain.TermSheet;
import com.arete.korbly.modules.termsheet.dto.TermSheetDTO;
import com.arete.korbly.modules.termsheet.mappers.TermSheetMapper;
import com.arete.korbly.modules.termsheet.persistence.TermSheetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TermSheetService implements ITermSheetService{
    private final TermSheetRepository termSheetRepository;
    private final TermSheetMapper termSheetMapper;

    public TermSheetService(TermSheetRepository termSheetRepository,
                            TermSheetMapper termSheetMapper) {
        this.termSheetRepository = termSheetRepository;
        this.termSheetMapper = termSheetMapper;
    }

    @Override
    public TermSheet createTermSheet(TermSheetDTO dto, UUID createdByUserId) {
        return null;
    }

    @Override
    public TermSheet amendTermSheet(UUID parentId, TermSheetDTO dto, UUID createdByUserId) {
        return null;
    }

    @Override
    public TermSheet getTermSheetById(UUID termSheetId) {
        return null;
    }

    @Override
    public List<TermSheet> getAllVersions(UUID parentId) {
        return List.of();
    }

    @Override
    public TermSheet getLatestVersion(UUID parentId) {
        return null;
    }

    @Override
    public TermSheet updateTermSheet(UUID termSheetId, TermSheetDTO dto) {
        return null;
    }

    @Override
    public void signTermSheet(UUID termSheetId, UUID signedByUserId) {

    }

    @Override
    public void markAsDeleted(UUID termSheetId) {

    }

    @Override
    public void markAsLatest(UUID termSheetId) {

    }

    @Override
    public List<TermSheet> findByDeal(UUID dealId) {
        return List.of();
    }

    @Override
    public List<TermSheet> findByTranche(UUID trancheId) {
        return List.of();
    }

    @Override
    public List<TermSheet> findBySME(UUID smeId) {
        return List.of();
    }
}

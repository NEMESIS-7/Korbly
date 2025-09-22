package com.arete.korbly.modules.termsheet.service;

import com.arete.korbly.modules.termsheet.domain.ConditionsPrecedent;
import com.arete.korbly.modules.termsheet.dto.ConditionPrecedentDTO;
import com.arete.korbly.modules.termsheet.enums.CPStatus;
import com.arete.korbly.modules.termsheet.mappers.ConditionPrecedentMapper;
import com.arete.korbly.modules.termsheet.persistence.ConditionsPrecedentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConditionsPrecedentService implements IConditionsPrecedentService{
    private final ConditionsPrecedentRepository conditionsPrecedentRepository;
    private final ConditionPrecedentMapper conditionPrecedentMapper;

    public ConditionsPrecedentService(ConditionsPrecedentRepository conditionsPrecedentRepository,
                                      ConditionPrecedentMapper conditionPrecedentMapper) {
        this.conditionsPrecedentRepository = conditionsPrecedentRepository;
        this.conditionPrecedentMapper = conditionPrecedentMapper;
    }

    @Override
    public ConditionsPrecedent addCondition(UUID sheetId, ConditionPrecedentDTO dto) {
        return null;
    }

    @Override
    public ConditionsPrecedent updateCondition(UUID cpId, ConditionPrecedentDTO dto) {
        return null;
    }

    @Override
    public void updateStatus(UUID cpId, CPStatus status, UUID approvedByUserId) {

    }

    @Override
    public ConditionsPrecedent getCondition(UUID cpId) {
        return null;
    }

    @Override
    public List<ConditionsPrecedent> getConditionsForSheet(UUID sheetId) {
        return List.of();
    }

    @Override
    public void waiveCondition(UUID cpId, String waiverReason, UUID approvedByUserId) {

    }

    @Override
    public void attachEvidence(UUID cpId, String evidenceFileKey) {

    }

    @Override
    public void markAsDeleted(UUID cpId) {

    }
}

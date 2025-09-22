package com.arete.korbly.modules.termsheet.service;

import com.arete.korbly.modules.termsheet.domain.ConditionsPrecedent;
import com.arete.korbly.modules.termsheet.dto.ConditionPrecedentDTO;
import com.arete.korbly.modules.termsheet.enums.CPStatus;

import java.util.List;
import java.util.UUID;

public interface IConditionsPrecedentService {


    ConditionsPrecedent addCondition(UUID sheetId, ConditionPrecedentDTO dto);
    ConditionsPrecedent updateCondition(UUID cpId, ConditionPrecedentDTO dto);
    void updateStatus(UUID cpId, CPStatus status, UUID approvedByUserId);


    ConditionsPrecedent getCondition(UUID cpId);
    List<ConditionsPrecedent> getConditionsForSheet(UUID sheetId);


    void waiveCondition(UUID cpId, String waiverReason, UUID approvedByUserId);
    void attachEvidence(UUID cpId, String evidenceFileKey);


    void markAsDeleted(UUID cpId);
}

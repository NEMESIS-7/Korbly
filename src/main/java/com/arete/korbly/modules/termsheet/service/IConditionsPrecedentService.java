package com.arete.korbly.modules.termsheet.service;

import com.arete.korbly.modules.termsheet.dto.CPRequest;
import com.arete.korbly.modules.termsheet.dto.CPResponse;
import com.arete.korbly.modules.termsheet.dto.ConditionPrecedentDTO;
import com.arete.korbly.modules.termsheet.enums.CPStatus;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface IConditionsPrecedentService {


    CPResponse addCondition(UUID sheetId, CPRequest request, HttpServletRequest servletRequest);
    CPResponse updateCondition(UUID cpId, ConditionPrecedentDTO dto, HttpServletRequest servletRequest);
    void updateStatus(UUID cpId, CPStatus status, UUID approvedByUserId, HttpServletRequest request);


    CPResponse getCondition(UUID cpId, HttpServletRequest request);
    List<CPResponse> getConditionsForSheet(UUID sheetId, HttpServletRequest request);


    void waiveCondition(UUID cpId, String waiverReason, UUID approvedByUserId, HttpServletRequest request);
    void attachEvidence(UUID cpId, String evidenceFileKey, HttpServletRequest request);


    void markAsDeleted(UUID cpId, HttpServletRequest request);
}

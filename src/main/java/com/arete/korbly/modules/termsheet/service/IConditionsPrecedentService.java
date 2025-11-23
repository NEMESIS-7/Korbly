package com.arete.korbly.modules.termsheet.service;

import com.arete.korbly.modules.termsheet.dto.CPRequest;
import com.arete.korbly.modules.termsheet.dto.CPResponse;
import com.arete.korbly.modules.termsheet.dto.ConditionPrecedentDTO;
import com.arete.korbly.modules.termsheet.enums.CPStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IConditionsPrecedentService {


    CPResponse addCondition(UUID sheetId, CPRequest request, MultipartFile evidenceFile) throws IOException;
    CPResponse updateCondition(UUID cpId, ConditionPrecedentDTO dto);
    void updateStatus(UUID cpId, CPStatus status, UUID approvedByUserId);


    CPResponse getCondition(UUID cpId);
    List<CPResponse> getConditionsForSheet(UUID sheetId);


    void waiveCondition(UUID cpId, String waiverReason, UUID approvedByUserId);
    void attachEvidence(UUID cpId, MultipartFile evidenceFile) throws IOException;


    void markAsDeleted(UUID cpId);
}

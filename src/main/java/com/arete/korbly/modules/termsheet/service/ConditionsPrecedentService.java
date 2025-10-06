package com.arete.korbly.modules.termsheet.service;

import com.arete.korbly.infrastructure.security.JWTService;
import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.shared.persistence.AppUserRepository;
import com.arete.korbly.modules.termsheet.domain.ConditionsPrecedent;
import com.arete.korbly.modules.termsheet.domain.TermSheet;
import com.arete.korbly.modules.termsheet.dto.CPRequest;
import com.arete.korbly.modules.termsheet.dto.CPResponse;
import com.arete.korbly.modules.termsheet.dto.ConditionPrecedentDTO;
import com.arete.korbly.modules.termsheet.enums.CPStatus;
import com.arete.korbly.modules.termsheet.exceptions.InvalidUpdate;
import com.arete.korbly.modules.termsheet.exceptions.TermSheetNotFound;
import com.arete.korbly.modules.termsheet.mappers.ConditionPrecedentMapper;
import com.arete.korbly.modules.termsheet.persistence.ConditionsPrecedentRepository;
import com.arete.korbly.modules.termsheet.persistence.TermSheetRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConditionsPrecedentService implements IConditionsPrecedentService{
    private final ConditionsPrecedentRepository conditionsPrecedentRepository;
    private final ConditionPrecedentMapper conditionPrecedentMapper;
    private final TermSheetRepository termSheetRepository;
    private final HttpServletRequest request;
    private final JWTService jwtService;
    private final AppUserRepository appUserRepository;

    public ConditionsPrecedentService(ConditionsPrecedentRepository conditionsPrecedentRepository,
                                      ConditionPrecedentMapper conditionPrecedentMapper,
                                      TermSheetRepository termSheetRepository,
                                      HttpServletRequest request,
                                      JWTService jwtService,
                                      AppUserRepository appUserRepository) {
        this.conditionsPrecedentRepository = conditionsPrecedentRepository;
        this.conditionPrecedentMapper = conditionPrecedentMapper;
        this.termSheetRepository = termSheetRepository;
        this.request = request;
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
    }

    private UUID extractAppUserId(HttpServletRequest request){
        return jwtService.extractAppUserId(request);
    }

    private Optional<AppUser> getAppUser(UUID appUserId){
        return appUserRepository.findAppUserById(appUserId);
    }

    @Override
    @Transactional
    public CPResponse addCondition(UUID sheetId, CPRequest dto, HttpServletRequest request) {
        // Fetch the TermSheet
        TermSheet termSheet = termSheetRepository.findById(sheetId)
                .orElseThrow(() -> new TermSheetNotFound("Term sheet with ID: " + sheetId + " not found"));

        // Extract current user
        UUID currentUserId = extractAppUserId(request);
        AppUser currentUser = getAppUser(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Map CPRequest to entity
        ConditionsPrecedent newCondition = conditionPrecedentMapper.toEntity(dto);

        // Set required fields
        newCondition.setSheet(termSheet);
        newCondition.setStatus(CPStatus.PENDING);
        newCondition.setEvidenceFileKey(null);
        newCondition.setCreatedBy(currentUser);
        newCondition.setWaiverReason(null);

        // Save the condition
        ConditionsPrecedent savedCondition = conditionsPrecedentRepository.save(newCondition);

        // Return response
        return conditionPrecedentMapper.toResponse(savedCondition);
    }

    @Override
    @Transactional
    public CPResponse updateCondition(UUID cpId, ConditionPrecedentDTO dto, HttpServletRequest request) {
        // Fetch the condition
        ConditionsPrecedent condition = conditionsPrecedentRepository.findById(cpId)
                .orElseThrow(() -> new InvalidUpdate("Condition precedent with ID: " + cpId + " not found"));

        // Check if condition is deleted
        if (condition.getDeleteYn() == DeleteYn.Y) {
            throw new InvalidUpdate("Cannot update a deleted condition");
        }

        // Check if condition is already approved/rejected - cannot be updated
        if (condition.getStatus() == CPStatus.APPROVED || condition.getStatus() == CPStatus.REJECTED) {
            throw new RuntimeException("Cannot update condition that has been approved or rejected");
        }

        // Update fields
        if (dto.title() != null) {
            condition.setTitle(dto.title());
        }
        if (dto.description() != null) {
            condition.setDescription(dto.description());
        }
        if (dto.required() != null) {
            condition.setRequired(dto.required());
        }
        if (dto.note() != null) {
            condition.setNote(dto.note());
        }

        // Save updated condition
        ConditionsPrecedent updatedCondition = conditionsPrecedentRepository.save(condition);

        return conditionPrecedentMapper.toResponse(updatedCondition);
    }

    @Override
    @Transactional
    public void updateStatus(UUID cpId, CPStatus status, UUID approvedByUserId, HttpServletRequest request) {
        // Fetch the condition
        ConditionsPrecedent condition = conditionsPrecedentRepository.findById(cpId)
                .orElseThrow(() -> new RuntimeException("Condition precedent with ID: " + cpId + " not found"));

        // Check if condition is deleted
        if (condition.getDeleteYn() == DeleteYn.Y) {
            throw new RuntimeException("Cannot update status of a deleted condition");
        }

        // Only allow PENDING -> APPROVED/REJECTED transitions
        if (condition.getStatus() != CPStatus.PENDING) {
            throw new RuntimeException("Can only update status of pending conditions");
        }

        // Validate status transition
        if (status != CPStatus.APPROVED && status != CPStatus.REJECTED) {
            throw new RuntimeException("Status can only be updated to APPROVED or REJECTED");
        }

        // Fetch approving user
        AppUser approvingUser = getAppUser(approvedByUserId)
                .orElseThrow(() -> new RuntimeException("Approving user not found"));

        // Update status and approver
        condition.setStatus(status);
        condition.setApprovedBy(approvingUser);

        conditionsPrecedentRepository.save(condition);
    }

    @Override
    @Transactional(readOnly = true)
    public CPResponse getCondition(UUID cpId, HttpServletRequest request) {
        // Fetch the condition
        ConditionsPrecedent condition = conditionsPrecedentRepository.findById(cpId)
                .orElseThrow(() -> new RuntimeException("Condition precedent with ID: " + cpId + " not found"));

        // Check if condition is deleted
        if (condition.getDeleteYn() == DeleteYn.Y) {
            throw new RuntimeException("Condition not found or has been deleted");
        }

        return conditionPrecedentMapper.toResponse(condition);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CPResponse> getConditionsForSheet(UUID sheetId, HttpServletRequest request) {
        // Verify term sheet exists
        TermSheet termSheet = termSheetRepository.findById(sheetId)
                .orElseThrow(() -> new TermSheetNotFound("Term sheet with ID: " + sheetId + " not found"));

        // Fetch all non-deleted conditions for the sheet
        List<ConditionsPrecedent> conditions = conditionsPrecedentRepository
                .findBySheetAndDeleteYn(termSheet, DeleteYn.N);

        return conditionPrecedentMapper.toResponseList(conditions);
    }

    @Override
    @Transactional
    public void waiveCondition(UUID cpId, String waiverReason, UUID approvedByUserId, HttpServletRequest request) {
        // Fetch the condition
        ConditionsPrecedent condition = conditionsPrecedentRepository.findById(cpId)
                .orElseThrow(() -> new RuntimeException("Condition precedent with ID: " + cpId + " not found"));

        // Check if condition is deleted
        if (condition.getDeleteYn() == DeleteYn.Y) {
            throw new RuntimeException("Cannot waive a deleted condition");
        }

        // Check if condition is already approved/rejected
        if (condition.getStatus() == CPStatus.APPROVED || condition.getStatus() == CPStatus.REJECTED) {
            throw new RuntimeException("Cannot waive a condition that has been approved or rejected");
        }

        // Fetch approving user
        AppUser approvingUser = getAppUser(approvedByUserId)
                .orElseThrow(() -> new RuntimeException("Approving user not found"));

        // Set waiver details
        condition.setWaiverReason(waiverReason);
        condition.setStatus(CPStatus.WAIVED);
        condition.setApprovedBy(approvingUser);

        conditionsPrecedentRepository.save(condition);
    }

    @Override
    @Transactional
    public void attachEvidence(UUID cpId, String evidenceFileKey, HttpServletRequest request) {
        // Fetch the condition
        ConditionsPrecedent condition = conditionsPrecedentRepository.findById(cpId)
                .orElseThrow(() -> new RuntimeException("Condition precedent with ID: " + cpId + " not found"));

        // Check if condition is deleted
        if (condition.getDeleteYn() == DeleteYn.Y) {
            throw new RuntimeException("Cannot attach evidence to a deleted condition");
        }

        // Check if evidence already exists
        if (condition.getEvidenceFileKey() != null && !condition.getEvidenceFileKey().isEmpty()) {
            throw new RuntimeException("Evidence has already been attached to this condition. Cannot attach again.");
        }

        // Attach evidence
        condition.setEvidenceFileKey(evidenceFileKey);

        conditionsPrecedentRepository.save(condition);
    }

    @Override
    @Transactional
    public void markAsDeleted(UUID cpId, HttpServletRequest request) {
        // Fetch the condition
        ConditionsPrecedent condition = conditionsPrecedentRepository.findById(cpId)
                .orElseThrow(() -> new RuntimeException("Condition precedent with ID: " + cpId + " not found"));

        // Check if already deleted
        if (condition.getDeleteYn() == DeleteYn.Y) {
            throw new RuntimeException("Condition is already deleted");
        }

        // Soft delete
        condition.setDeleteYn(DeleteYn.Y);

        conditionsPrecedentRepository.save(condition);
    }
}
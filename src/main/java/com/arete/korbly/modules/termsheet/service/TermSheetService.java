package com.arete.korbly.modules.termsheet.service;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.shared.exceptions.SMENotFound;
import com.arete.korbly.modules.shared.exceptions.UserNotFound;
import com.arete.korbly.modules.shared.persistence.AppUserRepository;
import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.sme.persistence.SMERepository;
import com.arete.korbly.modules.syndication.domain.Deal;
import com.arete.korbly.modules.syndication.domain.Tranche;
import com.arete.korbly.modules.syndication.exceptions.DealNotFound;
import com.arete.korbly.modules.syndication.exceptions.TrancheNotFound;
import com.arete.korbly.modules.syndication.persistence.DealRepository;
import com.arete.korbly.modules.syndication.persistence.TrancheRepository;
import com.arete.korbly.modules.termsheet.domain.ConditionsPrecedent;
import com.arete.korbly.modules.termsheet.domain.TermSheet;
import com.arete.korbly.modules.termsheet.dto.TermSheetDTO;
import com.arete.korbly.modules.termsheet.dto.TermSheetResponse;
import com.arete.korbly.modules.termsheet.enums.CPStatus;
import com.arete.korbly.modules.termsheet.enums.TermSheetStatus;
import com.arete.korbly.modules.termsheet.exceptions.TermSheetNotFound;
import com.arete.korbly.modules.termsheet.mappers.TermSheetMapper;
import com.arete.korbly.modules.termsheet.persistence.ConditionsPrecedentRepository;
import com.arete.korbly.modules.termsheet.persistence.TermSheetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TermSheetService implements ITermSheetService {
    private final TermSheetRepository termSheetRepository;
    private final TermSheetMapper termSheetMapper;
    private final DealRepository dealRepository;
    private final TrancheRepository trancheRepository;
    private final SMERepository smeRepository;
    private final AppUserRepository appUserRepository;
    private final ConditionsPrecedentRepository conditionsPrecedentRepository;
    private final TermsheetDocumentServiceImpl documentService;


    public TermSheetService(TermSheetRepository termSheetRepository,
                            TermSheetMapper termSheetMapper,
                            DealRepository dealRepository,
                            TrancheRepository trancheRepository,
                            SMERepository smeRepository,
                            AppUserRepository appUserRepository,
                            ConditionsPrecedentRepository conditionsPrecedentRepository,
                            TermsheetDocumentServiceImpl documentService) {
        this.termSheetRepository = termSheetRepository;
        this.termSheetMapper = termSheetMapper;
        this.dealRepository = dealRepository;
        this.trancheRepository = trancheRepository;
        this.smeRepository = smeRepository;
        this.appUserRepository = appUserRepository;
        this.conditionsPrecedentRepository = conditionsPrecedentRepository;
        this.documentService = documentService;
    }

    @Override
    public TermSheetResponse createTermSheet(TermSheetDTO dto, UUID createdByUserId) {
        Optional<AppUser> createdBy = appUserRepository.findAppUserById(createdByUserId);
        if (createdBy.isEmpty()) {
            throw new UserNotFound("User with ID: " + dto.createdBy() + " not found and hence term sheet cannot be created.");
        }

        Optional<SME> sme = smeRepository.findSMEBySmeId(dto.smeId());
        if (sme.isEmpty()) {
            throw new SMENotFound("SME with ID: " + dto.smeId() + " not found and hence term sheet cannot be created.");
        }

        Optional<Tranche> tranche = trancheRepository.findById(dto.trancheId());
        if (tranche.isEmpty()) {
            throw new TrancheNotFound("Tranche with ID: " + dto.trancheId() + " not found and hence term sheet cannot be created.");
        }

        Optional<Deal> deal = dealRepository.findDealById(dto.dealId());
        if (deal.isEmpty()) {
            throw new DealNotFound("Deal with ID: " + dto.dealId() + " not found and hence term sheet cannot be created.");
        }

        TermSheet newTermsheet = termSheetMapper.toEntity(dto);
        newTermsheet.setSheetVersion(1);
        newTermsheet.setCreatedBy(createdBy.get());
        newTermsheet.setLatest(Boolean.TRUE);
        newTermsheet.setSheetStatus(TermSheetStatus.DRAFT);
        newTermsheet.setSeniority(tranche.get().getTrancheType());
        newTermsheet.setDealId(deal.get());
        newTermsheet.setSmeId(sme.get());
        newTermsheet.setTrancheId(tranche.get());
        newTermsheet.setParent(newTermsheet);

        if (newTermsheet.getSheetVersion() == 1 && newTermsheet.getParentId() == null) {
            newTermsheet.setParentId(newTermsheet.getParent());
        }

        return termSheetMapper.toResponse(
                termSheetRepository.save(newTermsheet)
        );
    }

    @Override
    public TermSheetResponse amendTermSheet(UUID parentId, TermSheetDTO dto, UUID amendedBy) {
        TermSheet existingVersion = termSheetRepository.findLatestTermsheet(parentId)
                .orElseThrow(() -> new TermSheetNotFound("Term sheet with parent ID: " + parentId + " not found"));
        existingVersion.setLatest(Boolean.FALSE);

        AppUser amender = appUserRepository.findById(amendedBy)
                .orElseThrow(() -> new UserNotFound("User amending this sheet not found and hence action cannot continue."));

        TermSheet latestVersion = termSheetMapper.copyAndAmend(existingVersion, dto);
        latestVersion.setSheetVersion(existingVersion.getSheetVersion() + 1);
        latestVersion.setParentId(existingVersion.getParentId());
        latestVersion.setLatest(Boolean.TRUE);
        latestVersion.setAmendedBy(amender);


        termSheetRepository.save(existingVersion);

        return termSheetMapper.toResponse(
                termSheetRepository.save(
                        latestVersion
                )
        );
    }

    @Override
    public TermSheetResponse getTermSheetById(UUID termSheetId) {
        TermSheet termSheet = termSheetRepository.getByTermSheetId(termSheetId)
                .orElseThrow(() -> new TermSheetNotFound("Term sheet with ID: " + termSheetId + " not found"));
        return termSheetMapper.toResponse(termSheet);
    }

    @Override
    public Page<TermSheetResponse> getAllVersions(UUID parentId, Pageable pageable) {
        List<TermSheetResponse> content = termSheetRepository.getAllVersions(parentId)
                .stream()
                .map(termSheetMapper::toResponse)
                .toList();
        return new PageImpl<>(content, pageable, content.size());
    }

    @Override
    public TermSheetResponse getLatestVersion(UUID parentId) {
        return termSheetMapper
                .toResponse(termSheetRepository
                        .getLatestVersion(parentId));
    }

    @Override
    public TermSheetResponse updateTermSheet(UUID termSheetId, TermSheetDTO dto) {
        return null;
    }

    @Override
    public void signTermSheet(UUID termSheetId, UUID signedByUserId) {

        AppUser signingUser = appUserRepository.findById(signedByUserId)
                .orElseThrow(() -> new UserNotFound("User with ID: " + signedByUserId + " not found. Hence term sheet cannot be signed by this user."));
        TermSheet sheetToSign = termSheetRepository.findById(termSheetId)
                .orElseThrow(() -> new TermSheetNotFound("Term sheet with ID: " + termSheetId + " not found."));
        if (!sheetToSign.getSheetStatus().equals(TermSheetStatus.DRAFT)) {
            throw new TermSheetNotFound("Term sheet with ID: " + termSheetId + " has been signed already.");
        }
        if (!sheetToSign.getLatest().equals(Boolean.TRUE)) {
            throw new TermSheetNotFound("This is not the latest version of this term sheet hence it cannot be signed.");
        }
        sheetToSign.setSignedBy(signingUser);
        sheetToSign.setSheetStatus(TermSheetStatus.EXECUTED);
        sheetToSign.setSignedAt(Timestamp.from(Instant.now()));

        if (sheetToSign.getConditionsPrecedent() == null || sheetToSign.getConditionsPrecedent().isEmpty()) {
            sheetToSign.setConditionsPrecedent(generateDefaultCPs(sheetToSign));
        }
        termSheetRepository.save(sheetToSign);
    }

    private List<ConditionsPrecedent> generateDefaultCPs(TermSheet termSheet) {
        List<ConditionsPrecedent> conditionsPrecedent = List.of(
                new ConditionsPrecedent(termSheet,
                        "Board Resolution",
                        "Board resolution authorizing the borrowing and execution of loan documents.",
                        true, CPStatus.PENDING, null, null, null,
                        Timestamp.from(Instant.now()), null, DeleteYn.N),

                new ConditionsPrecedent(termSheet,
                        "Insurance Certificate",
                        "Proof of insurance covering pledged collateral with lender as loss payee.",
                        true, CPStatus.PENDING, null, null, null,
                        Timestamp.from(Instant.now()), null, DeleteYn.N),

                new ConditionsPrecedent(termSheet,
                        "Collateral Perfection",
                        "Filing of security documents and registration with relevant registry.",
                        true,
                        CPStatus.PENDING,
                        null,
                        null,
                        null,
                        Timestamp.from(Instant.now()),
                        null, DeleteYn.N),

                new ConditionsPrecedent(termSheet,
                        "KYC/KYB Documentation",
                        "Completed Know Your Customer (KYC) and Know Your Business compliance checks.",
                        true, CPStatus.PENDING, null, null, null,
                        Timestamp.from(Instant.now()),
                        null,
                        DeleteYn.N),

                new ConditionsPrecedent(termSheet,
                        "Tax Clearance Certificate",
                        "Valid tax clearance certificate from relevant authority.",
                        true, CPStatus.PENDING, null, null, null,
                        Timestamp.from(Instant.now()), null, DeleteYn.N)
        );
        return conditionsPrecedentRepository.saveAll(conditionsPrecedent);
    }

    @Override
    public void markAsDeleted(UUID termSheetId) {
        TermSheet sheetToDelete = termSheetRepository.findById(termSheetId)
                .orElseThrow(() -> new TermSheetNotFound("Term sheet with ID: " + termSheetId + " not found"));
        sheetToDelete.setDeleteYn(DeleteYn.Y);
        termSheetRepository.save(sheetToDelete);
    }

    @Override
    public void markAsLatest(UUID termSheetId) {
        TermSheet sheetToMark = termSheetRepository.findById(termSheetId)
                .orElseThrow(() -> new TermSheetNotFound("Term sheet with ID: " + termSheetId + " not found"));
        sheetToMark.setLatest(Boolean.TRUE);


        termSheetRepository.markAllAsNotLatest(sheetToMark.getParent().getTermSheetId());
        sheetToMark.setLatest(true);
        termSheetRepository.save(sheetToMark);

        termSheetRepository.save(sheetToMark);
    }

    @Override
    public Page<TermSheetResponse> findByDeal(UUID dealId, Pageable pageable) {
        List<TermSheetResponse> content = termSheetRepository.findByDealID(dealId)
                .stream()
                .map(termSheetMapper::toResponse)
                .toList();
        return new PageImpl<>(content, pageable, content.size());
    }

    @Override
    public Page<TermSheetResponse> findByTranche(UUID trancheId, Pageable pageable) {
        List<TermSheetResponse> content = termSheetRepository
                .findByTrancheId(trancheId)
                .stream()
                .map(termSheetMapper::toResponse)
                .toList();
        return new PageImpl<>(content, pageable, content.size());
    }

    @Override
    public Page<TermSheetResponse> findBySME(UUID smeId, Pageable pageable) {
        List<TermSheetResponse> content = termSheetRepository
                .findBySmeId(smeId)
                .stream()
                .map(termSheetMapper::toResponse)
                .toList();
        return new PageImpl<>(content, pageable, content.size());
    }
}

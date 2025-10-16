package com.arete.korbly.modules.regulator.service;

import com.arete.korbly.infrastructure.security.JWTService;
import com.arete.korbly.modules.regulator.domain.AuditLog;
import com.arete.korbly.modules.regulator.domain.Regulator;
import com.arete.korbly.modules.regulator.dto.*;
import com.arete.korbly.modules.regulator.enums.RegulatorStatus;
import com.arete.korbly.modules.regulator.mapper.AuditLogMapper;
import com.arete.korbly.modules.regulator.mapper.RegulatorMapper;
import com.arete.korbly.modules.regulator.persistence.AuditLogRepository;
import com.arete.korbly.modules.regulator.persistence.RegulatorRepository;
import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.SMEIndustry;
import com.arete.korbly.modules.shared.enums.UserType;
import com.arete.korbly.modules.shared.exceptions.UnauthorizedAccess;
import com.arete.korbly.modules.shared.exceptions.UserAlreadyExists;
import com.arete.korbly.modules.shared.exceptions.UserNotFound;
import com.arete.korbly.modules.shared.persistence.AppUserRepository;
import com.arete.korbly.modules.syndication.domain.Allocation;
import com.arete.korbly.modules.syndication.domain.Deal;
import com.arete.korbly.modules.syndication.domain.Tranche;
import com.arete.korbly.modules.syndication.enums.DealStatus;
import com.arete.korbly.modules.syndication.exceptions.DealNotFound;
import com.arete.korbly.modules.syndication.persistence.AllocationRepository;
import com.arete.korbly.modules.syndication.persistence.DealRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegulatorService implements IRegulatorService {
    private final HttpServletRequest request;
    private final RegulatorRepository regulatorRepository;
    private final AuditLogRepository auditLogRepository;
    private final JWTService jwtService;
    private final AppUserRepository appUserRepository;
    private final RegulatorMapper regulatorMapper;
    private final DealRepository dealRepository;
    private final AllocationRepository allocationRepository;
    private final AuditLogMapper auditLogMapper;

    public RegulatorService(HttpServletRequest request,
                            RegulatorRepository regulatorRepository,
                            AuditLogRepository auditLogRepository,
                            JWTService jwtService,
                            AppUserRepository appUserRepository,
                            RegulatorMapper regulatorMapper,
                            DealRepository dealRepository,
                            AllocationRepository allocationRepository, AuditLogMapper auditLogMapper) {
        this.request = request;
        this.regulatorRepository = regulatorRepository;
        this.auditLogRepository = auditLogRepository;
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
        this.regulatorMapper = regulatorMapper;
        this.dealRepository = dealRepository;
        this.allocationRepository = allocationRepository;
        this.auditLogMapper = auditLogMapper;
    }

    @Override
    public RegulatorDTO createRegulator(CreateRegulatorDTO dto, UUID adminId) {
        AppUser admin = appUserRepository.findAppUserById(adminId)
                .orElseThrow(() -> new UserNotFound("User account does not exist."));

        if (!admin.getUserType().equals(UserType.ADMIN)) {
            throw new UnauthorizedAccess("User is not admin");
        }

        Optional<AppUser> potentialExisting = appUserRepository.findByPrimaryContactEmail(dto.regulatorEmail());
        if (potentialExisting.isPresent()) {
            throw new UserAlreadyExists("User with this email already exists as a: " + potentialExisting.get().getUserType().toString());
        }

        AppUser regulatorUser = AppUser.builder()
                .primaryContactEmail(dto.regulatorEmail())
                .userType(UserType.REGULATORY_AUTHORITY)
                .build();
        appUserRepository.save(regulatorUser);

        //create the new regulator
        Regulator newRegulator = Regulator.builder()
                .regulatorName(dto.regulatorName())
                .regulatorJurisdiction(dto.regulatorJurisdiction())
                .regulatorType(dto.regulatorType())
                .regulatorStatus(RegulatorStatus.ACTIVE)
                .appUser(regulatorUser)
                .createdBy(admin)
                .build();

        return regulatorMapper
                .entityToDTO(
                        regulatorRepository.save(newRegulator)
                );
    }

    @Override
    public Page<RegulatorDTO> listAllRegulators(Pageable pageable) {
        Page<Regulator> regulators = regulatorRepository.getAllRegulators(pageable);

        List<RegulatorDTO> regulatorDTOS = regulators
                .getContent()
                .stream()
                .map(regulatorMapper::entityToDTO)
                .toList();

        return new PageImpl<>(regulatorDTOS, pageable, regulators.getTotalElements());
    }

    @Override
    public RegulatorDTO updateRegulatorStatus(UUID regulatorId, RegulatorStatus regulatorStatus) {
        Regulator regulatorToUpdate = regulatorRepository.findByAppUserId(regulatorId)
                .orElseThrow(() -> new UserNotFound("Regulator account with ID: " + regulatorId + " not found"));

        regulatorToUpdate.setRegulatorStatus(regulatorStatus);

        return regulatorMapper
                .entityToDTO(
                        regulatorRepository
                                .save(regulatorToUpdate)
                );
    }

    @Override
    public Page<RegulatorDealViewDTO> getDealsForRegulator(UUID regulatorId, Pageable pageable) {
        Regulator regulator = regulatorRepository.findByAppUserId(regulatorId)
                .orElseThrow(() -> new UserNotFound("Regulator account not found"));

        if (!regulator.getRegulatorStatus().equals(RegulatorStatus.ACTIVE)) {
            throw new UnauthorizedAccess("Regulator account is inactive");
        }
        Page<Deal> deals = dealRepository.listAllDeals(pageable);

        List<RegulatorDealViewDTO> dealViewDTOS = deals
                .getContent()
                .stream()
                .map(this::toRegulatorDealView)
                .toList();
        return new PageImpl<>(dealViewDTOS, pageable, deals.getTotalElements());
    }

    @Override
    public RegulatorDealViewDTO getDealDetailForRegulator(UUID regulatorId, UUID dealId) {
        //build regulator allocation view
        // build allocator tranche view
        // build allocator deal view
        Regulator regulator = regulatorRepository.findByAppUserId(regulatorId)
                .orElseThrow(() -> new UserNotFound("Regulator account not found"));
        if (!regulator.getRegulatorStatus().equals(RegulatorStatus.ACTIVE)) {
            throw new UnauthorizedAccess("Regulator account is not active.");
        }
        Deal deal = dealRepository.findDealById(dealId)
                .orElseThrow(() -> new DealNotFound("Deal with ID: " + dealId + " not found."));
        return toRegulatorDealView(deal);
    }

    @Override
    public Page<AuditLogDTO> getAuditLogsForEntity(UUID regulatorId, String entityType, UUID entityId, Pageable pageable) {
        Regulator regulator = regulatorRepository.findByAppUserId(regulatorId)
                .orElseThrow(() -> new UserNotFound("Regulator account not found"));
        if (!regulator.getRegulatorStatus().equals(RegulatorStatus.ACTIVE)) {
            throw new UnauthorizedAccess("Regulator account is not active.");
        }

        Page<AuditLog> auditLogs = auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId, pageable);

        List<AuditLogDTO> auditLogDTOS = auditLogs.getContent()
                .stream()
                .map(auditLogMapper::entityToDTO)
                .toList();

        return new PageImpl<>(auditLogDTOS, pageable, auditLogDTOS.size());
    }

    @Override
    public Page<AuditLogDTO> getAllAuditLogs(UUID regulatorId, Pageable pageable) {
        Regulator regulator = regulatorRepository.findByAppUserId(regulatorId)
                .orElseThrow(() -> new UserNotFound("Regulator account not found"));
        if (!regulator.getRegulatorStatus().equals(RegulatorStatus.ACTIVE)) {
            throw new UnauthorizedAccess("Regulator account is not active.");
        }

        Page<AuditLog> allLogs = auditLogRepository.findAll(pageable);

        List<AuditLogDTO> auditLogDTOS = allLogs.getContent()
                .stream()
                .map(auditLogMapper::entityToDTO)
                .toList();
        return new PageImpl<>(auditLogDTOS, pageable, allLogs.getTotalElements());
    }

    @Override
    public Page<RegulatorDealViewDTO> searchDeals(UUID regulatorId, SMEIndustry sector, DealStatus status, Pageable pageable) {
        Regulator regulator = regulatorRepository.findByAppUserId(regulatorId)
                .orElseThrow(() -> new UserNotFound("Regulator account not found"));
        if (!regulator.getRegulatorStatus().equals(RegulatorStatus.ACTIVE)) {
            throw new UnauthorizedAccess("Regulator account is not active.");
        }
        Page<Deal> dealPage;
        if (sector != null && status != null) {
            dealPage = dealRepository.findByDealSectorAndDealStatus(sector, status, pageable);
        } else if (sector != null && status == null) {
            dealPage = dealRepository.findByDealSector(sector, pageable);
        } else if (sector == null && status != null) {
            dealPage = dealRepository.findByDealStatus(status, pageable);
        } else {
            dealPage = dealRepository.listAllDeals(pageable);
        }

        List<RegulatorDealViewDTO> dealViewDTOList = dealPage
                .getContent()
                .stream()
                .map(this::toRegulatorDealView)
                .toList();

        return new PageImpl<>(dealViewDTOList, pageable, dealPage.getTotalElements());
    }

    private RegulatorDealViewDTO toRegulatorDealView(Deal deal) {
        List<RegulatorTrancheViewDTO> trancheViews = new ArrayList<>();

        for (Tranche tranche : deal.getTranches()) {
            List<RegulatorAllocationViewDTO> allocations = new ArrayList<>();
            BigDecimal allocatedSoFar = BigDecimal.ZERO;

            List<Allocation> trancheAllocations = allocationRepository.findAllocationsByTrancheId(tranche.getTrancheId());
            for (Allocation alloc : trancheAllocations) {
                allocatedSoFar = allocatedSoFar.add(alloc.getAmount());

                RegulatorAllocationViewDTO allocationViewDTO = new RegulatorAllocationViewDTO(
                        alloc.getAllocationId(),
                        alloc.getInvestorId().getInvestorId(),
                        alloc.getInvestorId().getInvestorType(),
                        alloc.getAmount(),
                        alloc.getAllocationStatus(),
                        alloc.getConfirmedBy() != null ? alloc.getConfirmedBy().getUserId() : null, // safe null check
                        alloc.getConfirmedAt()
                );
                allocations.add(allocationViewDTO);
            }

            BigDecimal remainingCapacity = tranche.getAmount().subtract(allocatedSoFar);

            trancheViews.add(new RegulatorTrancheViewDTO(
                    tranche.getTrancheId(),
                    tranche.getTrancheType(),
                    tranche.getAmount(),
                    allocatedSoFar,
                    remainingCapacity,
                    BigDecimal.valueOf(tranche.getInterestRate()),
                    tranche.getTenorMonths(),
                    allocations
            ));
        }

        return new RegulatorDealViewDTO(
                deal.getDealId(),
                deal.getDealTitle(),
                deal.getDealDescription(),
                deal.getDealSector(),
                deal.getCurrency(),
                deal.getTotalAmount(),
                trancheViews
        );
    }
}

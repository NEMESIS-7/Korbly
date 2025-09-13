package com.arete.korbly.modules.regulator.service;

import com.arete.korbly.infrastructure.security.JWTService;
import com.arete.korbly.modules.regulator.domain.Regulator;
import com.arete.korbly.modules.regulator.dto.*;
import com.arete.korbly.modules.regulator.enums.RegulatorStatus;
import com.arete.korbly.modules.regulator.mapper.RegulatorMapper;
import com.arete.korbly.modules.regulator.persistence.AuditLogRepository;
import com.arete.korbly.modules.regulator.persistence.RegulatorRepository;
import com.arete.korbly.modules.shared.domain.AppUser;
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

    public RegulatorService(HttpServletRequest request,
                            RegulatorRepository regulatorRepository,
                            AuditLogRepository auditLogRepository,
                            JWTService jwtService,
                            AppUserRepository appUserRepository,
                            RegulatorMapper regulatorMapper,
                            DealRepository dealRepository,
                            AllocationRepository allocationRepository) {
        this.request = request;
        this.regulatorRepository = regulatorRepository;
        this.auditLogRepository = auditLogRepository;
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
        this.regulatorMapper = regulatorMapper;
        this.dealRepository = dealRepository;
        this.allocationRepository = allocationRepository;
    }

    @Override
    public RegulatorDTO createRegulator(CreateRegulatorDTO dto, UUID adminId) {
        AppUser admin = appUserRepository.findAppUserById(adminId)
                .orElseThrow(() -> new UserNotFound("User account does not exist."));

        if (!admin.getUserType().equals(UserType.ADMIN)){
            throw new UnauthorizedAccess("User is not admin");
        }

        Optional<AppUser> potentialExisting = appUserRepository.findByPrimaryContactEmail(dto.regulatorEmail());
        if (potentialExisting.isPresent()){
            throw new UserAlreadyExists("User with this email already exists as a: " + potentialExisting.get().getUserType().toString());
        }

        AppUser regulatorUser = AppUser.builder()
                .primaryContactEmail(dto.regulatorEmail())
                .userType(UserType.REGULATORY_AUTHORITY)
                .build();


        //create the new regulator
        Regulator newRegulator = Regulator.builder()
                .regulatorName(dto.regulatorName())
                .regulatorJurisdiction(dto.regulatorJurisdiction())
                .regulatorType(dto.regulatorType())
                .regulatorStatus(RegulatorStatus.ACTIVE)
                .appUser(regulatorUser)
                .build();

        return regulatorMapper
                .entityToDTO(
                regulatorRepository.save(newRegulator)
        );
    }

    @Override
    public Page<RegulatorDTO> listAllRegulators(Pageable pageable) {
        Page<Regulator> regulators = regulatorRepository.getAllRegulators(pageable);

        List<RegulatorDTO> regulatorDTOS = regulators.getContent()
                .stream()
                .map(regulatorMapper::entityToDTO)
                .toList();

        return new PageImpl<>(regulatorDTOS, pageable, regulators.getTotalElements());
    }

    @Override
    public RegulatorDTO updateRegulatorStatus(UUID regulatorId, RegulatorStatus regulatorStatus) {
        Regulator regulatorToUpdate = regulatorRepository.findById(regulatorId)
                .orElseThrow(() -> new UserNotFound("Regulator account with ID: " + regulatorId + " not found"));

        regulatorToUpdate.setRegulatorStatus(regulatorStatus);

        return regulatorMapper.entityToDTO(regulatorToUpdate);
    }

    @Override
    public Page<RegulatorDealViewDTO> getDealsForRegulator(UUID regulatorId, Pageable pageable) {
        Regulator regulator = regulatorRepository.findById(regulatorId)
                .orElseThrow(() -> new UserNotFound("Regulator account not found"));
        if (!regulator.getRegulatorStatus().equals(RegulatorStatus.ACTIVE)){
            throw new UnauthorizedAccess("Regulator account is inactive");
        }
        Page<Deal> deals = dealRepository.listAllDeals(pageable);
        List<Deal> dealList = deals.getContent()
                .stream()
                .toList();
        List<RegulatorDealViewDTO> dealView = new ArrayList<>();
        for(Deal deal : dealList){
            List<RegulatorTrancheViewDTO> trancheViews = new ArrayList<>();
            for(Tranche tranche : deal.getTranches()){
                List<RegulatorAllocationViewDTO> allocations = new ArrayList<>();
                BigDecimal allocatedSoFar = BigDecimal.ZERO;
                List<Allocation> allocation = allocationRepository.findAllocationsByTrancheId(tranche.getTrancheId());
                for(Allocation alloc : allocation){
                    allocatedSoFar = allocatedSoFar.add(alloc.getAmount());

                    RegulatorAllocationViewDTO allocationViewDTO = new RegulatorAllocationViewDTO(
                            alloc.getAllocationId(),
                            alloc.getInvestorId().getInvestorId(),
                            alloc.getInvestorId().getInvestorType(),
                            alloc.getAmount(),
                            alloc.getAllocationStatus(),
                            alloc.getConfirmedBy().getUserId() != null ? alloc.getConfirmedBy().getUserId() : null,
                            alloc.getConfirmedAt()
                    );
                    allocations.add(allocationViewDTO);
                }
                BigDecimal remainingCapacity = tranche.getAmount().subtract(allocatedSoFar);

                RegulatorTrancheViewDTO regulatorTrancheViewDTO = new RegulatorTrancheViewDTO(
                        tranche.getTrancheId(),
                        tranche.getTrancheType(),
                        tranche.getAmount(),
                        allocatedSoFar,
                        remainingCapacity,
                        tranche.getInterestRate(),
                        tranche.getTenorMonths(),
                        allocations
                );
                trancheViews.add(regulatorTrancheViewDTO);
            }
            RegulatorDealViewDTO regulatorDealViewDTO = new RegulatorDealViewDTO(
                    deal.getDealId(),
                    deal.getDealTitle(),
                    deal.getDealDescription(),
                    deal.getDealSector(),
                    deal.getCurrency(),
                    deal.getTotalAmount(),
                    trancheViews
            );
            dealView.add(regulatorDealViewDTO);
        }
        return new PageImpl<>(dealView, pageable, deals.getTotalElements());
    }

    @Override
    public RegulatorDealViewDTO getDealDetailForRegulator(UUID regulatorId, UUID dealId) {
        //build regulator allocation view
        // build allocator tranche view
        // build allocator deal view

        Regulator regulator = regulatorRepository.findById(regulatorId)
                .orElseThrow(() -> new UserNotFound("Regulator account not found"));
        if(!regulator.getRegulatorStatus().equals(RegulatorStatus.ACTIVE)){
            throw new UnauthorizedAccess("Regulator account is not active.");
        }

        Deal deal = dealRepository.findDealById(dealId)
                .orElseThrow(() -> new DealNotFound("Deal with ID: " + dealId + " not found,"));

        List<Tranche> dealTranches = deal.getTranches();

        List<RegulatorTrancheViewDTO> regulatorTrancheView = new ArrayList<>();
        for(Tranche tranche : dealTranches){
            List<RegulatorAllocationViewDTO> regulatorAllocationViewDTOS = new ArrayList<>();
            BigDecimal allocatedSoFar = BigDecimal.ZERO;

            List<Allocation> trancheAllocations = allocationRepository.findAllocationsByTrancheId(tranche.getTrancheId());
            for(Allocation allocation : trancheAllocations){
                allocatedSoFar = allocatedSoFar.add(allocation.getAmount());
                RegulatorAllocationViewDTO allocationViewDTO = new RegulatorAllocationViewDTO(
                        allocation.getAllocationId(),
                        allocation.getInvestorId().getInvestorId(),
                        allocation.getInvestorId().getInvestorType(),
                        allocation.getAmount(),
                        allocation.getAllocationStatus(),
                        allocation.getConfirmedBy().getUserId(),
                        allocation.getConfirmedAt()
                );
                regulatorAllocationViewDTOS.add(allocationViewDTO);

            }
            BigDecimal remainingCapacity = tranche.getAmount().subtract(allocatedSoFar);

            regulatorTrancheView.add(
                    new RegulatorTrancheViewDTO(
                            tranche.getTrancheId(),
                            tranche.getTrancheType(),
                            tranche.getAmount(),
                            allocatedSoFar,
                            remainingCapacity,
                            tranche.getInterestRate(),
                            tranche.getTenorMonths(),
                            regulatorAllocationViewDTOS
                    )
            );
        }
        return new RegulatorDealViewDTO(
                deal.getDealId(),
                deal.getDealTitle(),
                deal.getDealDescription(),
                deal.getDealSector(),
                deal.getCurrency(),
                deal.getTotalAmount(),
                regulatorTrancheView
        );
    }

    @Override
    public List<AuditLogDTO> getAuditLogsForEntity(UUID regulatorId, String entityType, UUID entityId, Pageable pageable) {
        return List.of();
    }

    @Override
    public List<AuditLogDTO> getAllAuditLogs(UUID regulatorId, Pageable pageable) {
        return List.of();
    }

    @Override
    public List<RegulatorDealViewDTO> searchDeals(UUID regulatorId, String sector, DealStatus status, Pageable pageable) {
        return List.of();
    }
}

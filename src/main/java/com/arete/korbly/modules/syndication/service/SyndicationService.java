package com.arete.korbly.modules.syndication.service;

import com.arete.korbly.modules.investor.domain.Investor;
import com.arete.korbly.modules.investor.persistence.InvestorRepository;
import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.shared.exceptions.InvestorNotFound;
import com.arete.korbly.modules.shared.exceptions.SMENotFound;
import com.arete.korbly.modules.shared.exceptions.UserNotFound;
import com.arete.korbly.modules.shared.persistence.AppUserRepository;
import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.sme.persistence.SMERepository;
import com.arete.korbly.modules.syndication.domain.Allocation;
import com.arete.korbly.modules.syndication.domain.Deal;
import com.arete.korbly.modules.syndication.domain.Tranche;
import com.arete.korbly.modules.syndication.dto.*;
import com.arete.korbly.modules.syndication.enums.AllocationStatus;
import com.arete.korbly.modules.syndication.enums.DealStatus;
import com.arete.korbly.modules.syndication.enums.TrancheStatus;
import com.arete.korbly.modules.syndication.exceptions.*;
import com.arete.korbly.modules.syndication.mapper.AllocationMapper;
import com.arete.korbly.modules.syndication.mapper.SyndicationMapper;
import com.arete.korbly.modules.syndication.persistence.AllocationRepository;
import com.arete.korbly.modules.syndication.persistence.DealRepository;
import com.arete.korbly.modules.syndication.persistence.TrancheRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class SyndicationService implements ISyndicationService{
    private final SMERepository smeRepository;
    private final DealRepository dealRepository;
    private final TrancheRepository trancheRepository;
    private final SyndicationMapper syndicationMapper;
    private final AppUserRepository appUserRepository;
    private final AllocationRepository allocationRepository;
    private final InvestorRepository investorRepository;
    private final AllocationMapper allocationMapper;

    public SyndicationService(SMERepository smeRepository,
                              DealRepository dealRepository,
                              TrancheRepository trancheRepository,
                              SyndicationMapper syndicationMapper,
                              AppUserRepository appUserRepository,
                              AllocationRepository allocationRepository, InvestorRepository investorRepository, AllocationMapper allocationMapper) {
        this.smeRepository = smeRepository;
        this.dealRepository = dealRepository;
        this.trancheRepository = trancheRepository;
        this.syndicationMapper = syndicationMapper;
        this.appUserRepository = appUserRepository;
        this.allocationRepository = allocationRepository;
        this.investorRepository = investorRepository;
        this.allocationMapper = allocationMapper;
    }

    @Override
    @Transactional
    public DealDTO createDeal(DealDTO dealDTO, AppUser createdBy) {
        SME smeInvolved = smeRepository.findSMEBySmeId(dealDTO.smeInvolved())
                .orElseThrow(() -> new SMENotFound("SME account with ID: " + dealDTO.smeInvolved() + " not found."));
        Deal newDeal = Deal.builder()
                .dealTitle(dealDTO.dealTitle())
                .dealDescription(dealDTO.dealDescription())
                .dealSector(dealDTO.dealSector())
                .totalAmount(dealDTO.totalAmount())
                .currency(dealDTO.currency())
                .dealStatus(DealStatus.OPEN)
                .smeInvolved(smeInvolved)
                .createdBy(createdBy)
                .build();

        if (dealDTO.tranches() != null && !dealDTO.tranches().isEmpty()){
            List<Tranche> dealTranches = dealDTO.tranches()
                    .stream()
                    .map(trancheDTO -> syndicationMapper.toTrancheEntity(trancheDTO, newDeal,  createdBy))
                    .toList();
            newDeal.setTranches(dealTranches);
        }
        return syndicationMapper
                .toDealDTO(dealRepository.save(newDeal));
    }

    @Transactional
    @Override
    public DealDTO updateDeal(UUID dealId, DealStatus dealStatus) {
        Deal dealToUpdate = dealRepository.findDealById(dealId)
                .orElseThrow(DealNotFound::new);
        if (dealToUpdate.getDealStatus().equals(DealStatus.OPEN) || dealToUpdate.getDealStatus().equals(DealStatus.DRAFT)){
            dealToUpdate.setDealStatus(dealStatus);
            return syndicationMapper
                    .toDealDTO(dealRepository.save(dealToUpdate));
        }
        throw new InvalidDealUpdate();
    }

    @Override
    public void deleteDeal(UUID dealId) {
        Deal dealToDelete = dealRepository.findDealById(dealId)
                        .orElseThrow(DealNotFound::new);
        List<Tranche> dealTranches = dealToDelete.getTranches();
        for(Tranche tranche : dealTranches){
            tranche.setDeleteYn(DeleteYn.Y);
        }
        dealToDelete.setDeleteYn(DeleteYn.Y);
        trancheRepository.saveAll(dealTranches);
        dealRepository.save(dealToDelete);
    }

    @Transactional
    @Override
    public TrancheDTO createTranche(UUID dealId, TrancheDTO trancheDTO, UUID createdByUserId) {
        Deal deal = dealRepository.findDealById(dealId)
                .orElseThrow(DealNotFound::new);

        AppUser createdBy = appUserRepository.findById(createdByUserId)
                .orElseThrow(() -> new UserNotFound("User with ID: " + createdByUserId + " not found."));
        if (deal.getDealStatus().equals(DealStatus.CLOSED)||!deal.getDealStatus().equals(DealStatus.OPEN)){
            throw new InvalidDealUpdate();
        }

        BigDecimal partOfDealFunded = deal.getTranches()
                .stream()
                .filter(tranche -> tranche.getDeleteYn().equals(DeleteYn.N))
                .map(Tranche::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal amountLeftToBeFunded = deal.getTotalAmount().subtract(partOfDealFunded);

        if (trancheDTO.amount().compareTo(amountLeftToBeFunded) > 0){
            throw new DealAmountExceeded();
        }else{
            Tranche newTranche = syndicationMapper.toTrancheEntity(trancheDTO, deal, createdBy);
            List<Tranche> existingTranches = deal.getTranches();

            existingTranches.add(newTranche);
            trancheRepository.save(newTranche);

            deal.setTranches(existingTranches);
            dealRepository.save(deal);

            return syndicationMapper.toTrancheDTO(newTranche);
        }
    }

    @Override
    public TrancheDTO updateTranche(UUID trancheId, TrancheDTO trancheDTO) {
        throw new UnsupportedOperationException("Tranches cannot be updated after creation");
    }

    @Transactional
    @Override
    public void deleteTranche(UUID trancheId) {
        trancheRepository
                .deleteTranche(trancheId);
    }

    @Override
    public DealDTO getDealById(UUID dealId) {
        Deal deal = dealRepository
                .findDealById(dealId)
                .orElseThrow(DealNotFound::new);
        return syndicationMapper
                .toDealDTO(deal);
    }

    @Transactional
    @Override
    public DealDTO moveDealToNextStage(UUID dealId) {

        //todo seek clarification on deal status changes
        //todo i.e moving from one stage to another...this works for now though

        Deal deal = dealRepository.findDealById(dealId)
                .orElseThrow(DealNotFound::new);

        if (deal.getDealStatus().equals(DealStatus.DRAFT)){
            deal.setDealStatus(DealStatus.OPEN);
        }else if(deal.getDealStatus().equals(DealStatus.OPEN)){
            deal.setDealStatus(DealStatus.CLOSED);
        }else{
            throw new DealStatusUpdateException(
                    "Deal in status " + deal.getDealStatus().getValue() +
                            " can only be moved to the immediate next stage."
            );
        }
        return syndicationMapper
                .toDealDTO(dealRepository.save(deal));
    }

    @Override
    public Page<DealDTO> getAllDeals(Pageable pageable){
        Page<Deal> page = dealRepository.listAllDeals(pageable);
        return page.map(syndicationMapper::toDealDTO);
    }

    @Override
    public Page<TrancheDTO> getAllTranches(Pageable pageable){
        Page<Tranche> tranches = trancheRepository.getAllTranches(pageable);
        return tranches.map(syndicationMapper::toTrancheDTO);
    }

    @Override
    public List<TrancheDTO> getSMETranches(UUID smeId){
        return trancheRepository.findBySME(smeId)
                .stream()
                .map(syndicationMapper::toTrancheDTO)
                .toList();
    }

    @Transactional
    public AllocationDTO allocateTrancheToInvestor(CreateAllocationDTO details) {
        Tranche trancheToUpdate = allocationRepository.findByTrancheIdForUpdate(details.trancheId())
                .orElseThrow(TrancheNotFound::new);
        if(trancheToUpdate != null && trancheToUpdate.getDeleteYn().equals(DeleteYn.Y)){
            throw new InvalidTrancheUpdate();
        }

        if(trancheToUpdate.getTrancheStatus().equals(TrancheStatus.FULLY_ALLOCATED)){
            throw new InvalidTrancheUpdate("Tranche has been allocated");
        }

        List<DealStatus> permittedDealStatus = List.of(
                DealStatus.OPEN,
                DealStatus.DRAFT,
                DealStatus.PUBLISHED
        );
        if(!permittedDealStatus.contains(trancheToUpdate.getDeal().getDealStatus())){
            throw new InvalidTrancheUpdate("Deal for this tranche is closed");
        }

        Investor investor = investorRepository.findById(details.investorId())
                .orElseThrow(InvestorNotFound::new);
        if(Boolean.FALSE.equals(investor.getInvestorVerified())){
            throw new UnverifiedInvestor("Investor is unverified and cannot continue with action");
        }

        BigDecimal alreadyAllocated  = allocationRepository.sumAllocatedAmountByTrancheId(details.trancheId())
                .orElse(BigDecimal.ZERO);

        BigDecimal remainderToBeFunded = trancheToUpdate.getAmount().subtract(alreadyAllocated);

        if(details.amount().compareTo(remainderToBeFunded) > 0){
            throw new InvalidAllocationAmount("Allocation exceeds tranche remaining capacity");
        }
        if(details.amount().compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidAllocationAmount("Allocation must be positive");
        }

/*        Optional<Allocation> possibleTrancheAllocation = allocationRepository.findAllocationByTrancheId(details.trancheId());
        if(possibleTrancheAllocation.isPresent()){
            throw new TrancheAlreadyAllocated("This tranche has already been allocated to an investor");
        }*/

        Allocation trancheAllocation = Allocation.builder()
                .trancheId(trancheToUpdate)
                .investorId(investor)
                .amount(details.amount())
                .allocationStatus(AllocationStatus.PENDING)
                .build();

        if(details.amount().compareTo(remainderToBeFunded) == 0){
            trancheToUpdate.setTrancheStatus(TrancheStatus.FULLY_ALLOCATED);
            trancheToUpdate.setIsAllocated(Boolean.TRUE);
        }else{
            trancheToUpdate.setTrancheStatus(TrancheStatus.PARTIALLY_ALLOCATED);
        }
//        trancheToUpdate.setTrancheStatus(TrancheStatus.ALLOCATED);
        trancheRepository.save(trancheToUpdate);

        return allocationMapper
                .mapEntityToDTO(
                        allocationRepository
                                .save(trancheAllocation)
                );
    }

    public AllocationDTO confirmAllocation(UUID allocationId, UUID adminId){
        Allocation confirmedAllocation = allocationRepository.findById(allocationId)
                .orElseThrow(AllocationNotFound::new);
        AppUser confirmedBy = appUserRepository.findById(adminId)
                        .orElseThrow(() -> new UserNotFound("User with ID: " + adminId + " not found."));
        confirmedAllocation.setAllocationStatus(AllocationStatus.CONFIRMED);
        confirmedAllocation.setConfirmedBy(confirmedBy);
        confirmedAllocation.setConfirmedAt(Timestamp.from(Instant.now()));

        return allocationMapper.mapEntityToDTO(
                allocationRepository.save(confirmedAllocation)
        );
    }

    public Set<InvestorDealViewDTO> getOpenDealsForInvestors(Pageable pageable){
        // 1. Fetch all OPEN deals
        // 2. For each deal, fetch its tranches
        // 3. For each tranche, calculate allocated amount and remaining capacity
        // 4. Filter out tranches with remainingCapacity <= 0
        // 5. If deal still has at least one tranche, map deal + tranche views to DTO
        // 6. Collect into Set<InvestorDealViewDTO> and return
        Set<InvestorDealViewDTO> result = new HashSet<>();

        List<Deal> openDeals = dealRepository.getOpenDeals();
        for(Deal deal : openDeals) {
            List<InvestorTrancheViewDTO> eligibleTranches = new ArrayList<>();

            List<Tranche> dealTranche = deal.getTranches();
            for (Tranche tranche : dealTranche) {
                List<Allocation> allocations = allocationRepository.findAllocationByTrancheId(tranche.getTrancheId());
                BigDecimal allocatedSoFar = BigDecimal.ZERO;
                for (Allocation allocation : allocations) {
                    allocatedSoFar = allocatedSoFar.add(allocation.getAmount());
                    System.out.println("amount allocated: " + allocatedSoFar);
                }

                System.out.println("allocated so far: " + allocatedSoFar);
                BigDecimal remainingCapacity = tranche.getAmount().subtract(allocatedSoFar);

                System.out.println("remaining capacity: " + remainingCapacity);

                if (remainingCapacity.compareTo(BigDecimal.ZERO) > 0) {
                    InvestorTrancheViewDTO trancheViewDTO = new InvestorTrancheViewDTO(
                            tranche.getTrancheId(),
                            tranche.getTrancheType().toString(),
                            tranche.getAmount(),
                            allocatedSoFar,
                            remainingCapacity,
                            BigDecimal.valueOf(tranche.getInterestRate()),
                            tranche.getTenorMonths()
                    );
                    eligibleTranches.add(trancheViewDTO);
                }
            }
            if (!eligibleTranches.isEmpty()) {
                InvestorDealViewDTO dealView = new InvestorDealViewDTO(
                        deal.getDealId(),
                        deal.getDealTitle(),
                        deal.getDealDescription(),
                        deal.getDealSector().toString(),
                        deal.getCurrency().toString(),
                        deal.getTotalAmount(),
                        eligibleTranches
                );
                result.add(dealView);
            }
        }
        return result;
    }

    public Page<AllocationDTO> getAllAllocations(Pageable pageable){
        Page<Allocation> allocations = allocationRepository.getAllAllocation(pageable);

        List<AllocationDTO> dtoContent = allocations.getContent()
                .stream()
                .map(allocationMapper::mapEntityToDTO)
                .toList();

        return new PageImpl<>(dtoContent, pageable, allocations.getTotalElements());

    }

    public Page<AllocationDTO> getAllocationsByTranche(UUID trancheId, Pageable pageable){
        Page<Allocation> allocations = allocationRepository.findAllocationsByTrancheId(trancheId, pageable);

        List<AllocationDTO> pageContent = allocations
                .getContent()
                .stream()
                .map(allocationMapper::mapEntityToDTO)
                .toList();

        return new PageImpl<>(
                pageContent,
                pageable,
                allocations.getTotalElements()
        );
    }

    public Page<AllocationDTO> findAllocationsByInvestorId(UUID investorId, Pageable pageable){
        Page<Allocation> allocations = allocationRepository.findAllocationsByInvestorId(investorId, pageable);

        List<AllocationDTO> pageContent = allocations
                .getContent()
                .stream()
                .map(allocationMapper::mapEntityToDTO)
                .toList();

        return new PageImpl<>(pageContent, pageable, allocations.getTotalElements());
    }
}

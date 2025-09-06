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
import com.arete.korbly.modules.syndication.dto.AllocationDTO;
import com.arete.korbly.modules.syndication.dto.DealDTO;
import com.arete.korbly.modules.syndication.dto.TrancheDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
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
                .orElseThrow(SMENotFound::new);
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
                .orElseThrow(UserNotFound::new);
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
    public AllocationDTO allocateTrancheToInvestor(AllocationDTO details) {
        Tranche trancheToUpdate = allocationRepository.findByTrancheIdForUpdate(details.trancheId())
                .orElseThrow(TrancheNotFound::new);
        if(trancheToUpdate != null && trancheToUpdate.getDeleteYn().equals(DeleteYn.Y)){
            throw new InvalidTrancheUpdate();
        }

        if(trancheToUpdate.getTrancheStatus().equals(TrancheStatus.ALLOCATED) || trancheToUpdate.getAllocated()){
            throw new InvalidTrancheUpdate("Tranche has been allocated");
        }

        List<DealStatus> permitedDealStatus = List.of(
                DealStatus.OPEN,
                DealStatus.DRAFT,
                DealStatus.PUBLISHED
        );
        if(!permitedDealStatus.contains(trancheToUpdate.getDeal().getDealStatus())){
            throw new InvalidTrancheUpdate("Deal for this tranche is closed");
        }

        Investor investor = investorRepository.findById(details.investorId())
                .orElseThrow(InvestorNotFound::new);
        if(!investor.getInvestorVerified()){
            throw new UnverifiedInvestor("Investor is unverified and cannot continue with action");
        }

        Optional<Allocation> possibleTrancheAllocation = allocationRepository.findAllocationByTrancheId(details.trancheId());
        if(possibleTrancheAllocation.isPresent()){
            throw new TrancheAlreadyAllocated("This tranche has already been allocated to an investor");
        }

        if(!details.amount().equals(trancheToUpdate.getAmount())){
            throw new InvalidAllocationAmount("Allocation amount must be equal to tranche amount");
        }

        Allocation trancheAllocation = Allocation.builder()
                .trancheId(trancheToUpdate)
                .investorId(investor)
                .amount(details.amount())
                .allocationStatus(AllocationStatus.PENDING)
                .build();

        trancheToUpdate.setAllocated(Boolean.TRUE);
        trancheToUpdate.setTrancheStatus(TrancheStatus.ALLOCATED);
        trancheRepository.save(trancheToUpdate);

        return allocationMapper
                .mapEntityToDTO(
                        allocationRepository
                                .save(trancheAllocation)
                );
    }
}

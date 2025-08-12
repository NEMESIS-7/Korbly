package com.arete.korbly.modules.syndication.service;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.shared.exceptions.SMENotFound;
import com.arete.korbly.modules.shared.exceptions.UserNotFound;
import com.arete.korbly.modules.shared.persistence.AppUserRepository;
import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.sme.persistence.SMERepository;
import com.arete.korbly.modules.syndication.domain.Deal;
import com.arete.korbly.modules.syndication.domain.Tranche;
import com.arete.korbly.modules.syndication.dto.DealDTO;
import com.arete.korbly.modules.syndication.dto.TrancheDTO;
import com.arete.korbly.modules.syndication.enums.DealStatus;
import com.arete.korbly.modules.syndication.exceptions.DealAmountExceeded;
import com.arete.korbly.modules.syndication.exceptions.DealNotFound;
import com.arete.korbly.modules.syndication.exceptions.DealStatusUpdateException;
import com.arete.korbly.modules.syndication.exceptions.InvalidDealUpdate;
import com.arete.korbly.modules.syndication.mapper.SyndicationMapper;
import com.arete.korbly.modules.syndication.persistence.DealRepository;
import com.arete.korbly.modules.syndication.persistence.TrancheRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class SyndicationService implements ISyndicationService{
    private final SMERepository smeRepository;
    private final DealRepository dealRepository;
    private final TrancheRepository trancheRepository;
    private final SyndicationMapper syndicationMapper;
    private final AppUserRepository appUserRepository;

    public SyndicationService(SMERepository smeRepository,
                              DealRepository dealRepository,
                              TrancheRepository trancheRepository,
                              SyndicationMapper syndicationMapper,
                              AppUserRepository appUserRepository) {
        this.smeRepository = smeRepository;
        this.dealRepository = dealRepository;
        this.trancheRepository = trancheRepository;
        this.syndicationMapper = syndicationMapper;
        this.appUserRepository = appUserRepository;
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
        trancheRepository.saveAll(dealTranches);
        dealRepository.deleteDealById(dealId);
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
}

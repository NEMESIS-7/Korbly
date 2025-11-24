package com.arete.korbly.modules.syndication.mapper;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.syndication.domain.Deal;
import com.arete.korbly.modules.syndication.domain.Tranche;
import com.arete.korbly.modules.syndication.dto.DealDTO;
import com.arete.korbly.modules.syndication.dto.TrancheDTO;
import com.arete.korbly.modules.syndication.enums.DealStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SyndicationMapper {

    public Tranche toTrancheEntity(TrancheDTO dto, Deal deal, AppUser createdBy) {
        Tranche tranche = new Tranche();
        tranche.setTrancheType(dto.trancheType());
        tranche.setAmount(dto.amount());
        tranche.setInterestRate(dto.interestRate().doubleValue());
        tranche.setTenorMonths(dto.tenorMonths());
        tranche.setIsAnchor(dto.isAnchor() != null && dto.isAnchor());
        tranche.setDeal(deal);
        tranche.setCreatedBy(createdBy);
        return tranche;
    }

    public Deal toDealEntity(DealDTO dto, SME sme, AppUser createdBy) {
        Deal deal = new Deal();
        deal.setDealTitle(dto.dealTitle());
        deal.setDealDescription(dto.dealDescription());
        deal.setDealSector(dto.dealSector());
        deal.setTotalAmount(dto.totalAmount());
        deal.setCurrency(dto.currency());
        deal.setDealStatus(dto.dealStatus() != null ? dto.dealStatus() : DealStatus.OPEN);
        deal.setSmeInvolved(sme);
        deal.setCreatedBy(createdBy);

        if (dto.tranches() != null) {
            List<Tranche> trancheEntities = dto.tranches().stream()
                    .map(trancheDTO -> toTrancheEntity(trancheDTO, deal, createdBy))
                    .toList();
            deal.setTranches(trancheEntities);
        }

        return deal;
    }

    public DealDTO toDealDTO(Deal deal) {
        return new DealDTO(
                deal.getDealId(),
                deal.getDealTitle(),
                deal.getDealDescription(),
                deal.getDealStatus(),
                deal.getCurrency(),
                deal.getTranches() != null ? deal.getTranches().stream()
                        .map(this::toTrancheDTO)
                        .toList()
                        : null,
                deal.getTotalAmount(),
                deal.getDealSector()
        );
    }

    public TrancheDTO toTrancheDTO(Tranche tranche) {
        return new TrancheDTO(
                tranche.getTrancheType(),
                tranche.getAmount(),
                BigDecimal.valueOf(tranche.getInterestRate()),
                tranche.getTenorMonths(),
                tranche.getIsAnchor(),
                tranche.getCreatedAt(),
                tranche.getUpdatedAt(),
                tranche.getTrancheStatus()
        );
    }

}

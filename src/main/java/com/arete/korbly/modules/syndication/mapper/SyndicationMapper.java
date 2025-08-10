package com.arete.korbly.modules.syndication.mapper;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.syndication.domain.Deal;
import com.arete.korbly.modules.syndication.domain.Tranche;
import com.arete.korbly.modules.syndication.dto.DealDTO;
import com.arete.korbly.modules.syndication.dto.TrancheDTO;
import com.arete.korbly.modules.syndication.enums.DealStatus;

import java.util.List;
import java.util.stream.Collectors;

public class SyndicationMapper {

    public static Deal toEntity(DealDTO dto, SME sme, AppUser createdBy) {
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
                    .map(trancheDTO -> toEntity(trancheDTO, deal, createdBy))
                    .collect(Collectors.toList());
            deal.setTranches(trancheEntities);
        }

        return deal;
    }

    public static Tranche toEntity(TrancheDTO dto, Deal deal, AppUser createdBy) {
        Tranche tranche = new Tranche();
        tranche.setTrancheType(dto.trancheType());
        tranche.setAmount(dto.amount());
        tranche.setInterestRate(dto.interestRate());
        tranche.setTenorMonths(dto.tenorMonths());
        tranche.setAnchor(dto.isAnchor() != null && dto.isAnchor());
        tranche.setDeal(deal);
        tranche.setCreatedBy(createdBy);
        return tranche;
    }
}

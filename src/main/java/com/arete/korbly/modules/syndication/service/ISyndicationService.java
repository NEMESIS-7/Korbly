package com.arete.korbly.modules.syndication.service;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.syndication.dto.DealDTO;
import com.arete.korbly.modules.syndication.dto.TrancheDTO;
import com.arete.korbly.modules.syndication.enums.DealStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ISyndicationService {
    /**
     * Deal
     * @param dealDTO deal details
     * @param createdBy the user who created the deal
     * @return a complete dealDTO
     */
    DealDTO createDeal(DealDTO dealDTO, AppUser createdBy);


    DealDTO updateDeal(UUID dealId, DealStatus dealStatus);
    void deleteDeal(UUID dealId); // soft delete


    TrancheDTO createTranche(UUID dealId, TrancheDTO trancheDTO, UUID createdByUserId);
    TrancheDTO updateTranche(UUID trancheId, TrancheDTO trancheDTO);
    void deleteTranche(UUID trancheId);

//    List<Deal> listDeals(DealFilterParams params); with computed metrics

    DealDTO getDealById(UUID dealId); // single deal view

    DealDTO moveDealToNextStage(UUID dealId);

    Page<DealDTO> getAllDeals(Pageable pageable);

    Page<TrancheDTO> getAllTranches(Pageable pageable);

    List<TrancheDTO> getSMETranches(UUID smeId);
}

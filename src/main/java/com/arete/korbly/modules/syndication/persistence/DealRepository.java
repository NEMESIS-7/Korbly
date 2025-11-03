package com.arete.korbly.modules.syndication.persistence;

import com.arete.korbly.modules.shared.enums.SMEIndustry;
import com.arete.korbly.modules.syndication.domain.Deal;
import com.arete.korbly.modules.syndication.enums.DealStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DealRepository extends JpaRepository<Deal, UUID> {

    @Modifying
    @Query("update Deal d set d.deleteYn = 'Y' where d.dealId = :dealId")
    void deleteDealById(UUID dealId);

    @Query("select d from Deal d where d.dealId = :dealId and d.deleteYn = 'N'")
    Optional<Deal> findDealById(UUID dealId);


    @Query("select d from Deal d where d.deleteYn = 'N'")
    Page<Deal> listAllDeals(Pageable pageable);

    @Query("select d from Deal d where d.dealStatus = 'OPEN' and d.deleteYn = 'N'")
    List<Deal> getOpenDeals();

    @Query("select d from Deal d where d.dealSector = :dealSector and d.deleteYn = 'N'")
    Page<Deal> findByDealSector(SMEIndustry dealSector, Pageable pageable);

    @Query("select d from Deal d where d.dealStatus = :dealStatus and d.deleteYn = 'N'")
    Page<Deal> findByDealStatus(DealStatus dealStatus, Pageable pageable);

    @Query("select d from Deal d where d.dealStatus = :dealSector and d.dealStatus = :status and d.deleteYn = 'N'")
    Page<Deal> findByDealSectorAndDealStatus(SMEIndustry dealSector, DealStatus status, Pageable pageable);

    @Query("select d from Deal d where d.smeInvolved.smeId = :smeInvolvedSmeId and d.deleteYn = 'N'")
    Page<Deal> findBySmeInvolved_SmeId(UUID smeInvolvedSmeId, Pageable pageable);
}

package com.arete.korbly.modules.syndication.persistence;

import com.arete.korbly.modules.syndication.domain.Tranche;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrancheRepository extends JpaRepository<Tranche, UUID> {

    @Modifying
    @Query("update Tranche t set t.deleteYn = 'Y' where t.trancheId = :trancheId")
    void deleteTranche(UUID trancheId);

    @Query("select t from Tranche t where t.deleteYn = 'N'")
    Page<Tranche> getAllTranches(Pageable pageable);

    @Query("select t from Tranche t where t.deal.smeInvolved.smeId = :smeId")
    List<Tranche> findBySME(UUID smeId);
}

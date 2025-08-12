package com.arete.korbly.modules.syndication.persistence;

import com.arete.korbly.modules.syndication.domain.Tranche;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrancheRepository extends JpaRepository<Tranche, UUID> {

    @Transactional
    @Modifying
    @Query("update Tranche t set t.deleteYn = 'Y'")
    void deleteTranche(UUID trancheId);

}

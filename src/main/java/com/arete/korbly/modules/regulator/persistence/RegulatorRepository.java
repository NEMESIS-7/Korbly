package com.arete.korbly.modules.regulator.persistence;

import com.arete.korbly.modules.regulator.domain.Regulator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegulatorRepository extends JpaRepository<Regulator, UUID> {
    @Query("select r from Regulator r where r.deleteYn = 'N'")
    Page<Regulator> getAllRegulators(Pageable pageable);

    @Query("select r from Regulator r where r.regulatorId = :regulatorId")
    Optional<Regulator> findById(UUID regulatorId);
}

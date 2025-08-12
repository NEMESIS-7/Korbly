package com.arete.korbly.modules.sme.persistence;

import com.arete.korbly.modules.sme.domain.SME;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SMERepository extends JpaRepository<SME, UUID> {
    @Query("select s from SME s where s.smeId = :smeId")
    Optional<SME> findSMEBySmeId(UUID smeId);
}

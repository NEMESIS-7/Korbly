package com.arete.korbly.modules.sme.persistence;

import com.arete.korbly.modules.sme.domain.SME;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SMERepository extends JpaRepository<SME, UUID> {

}

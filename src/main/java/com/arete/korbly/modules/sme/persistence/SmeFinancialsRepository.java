package com.arete.korbly.modules.sme.persistence;

import com.arete.korbly.modules.sme.domain.SmeMonthlyFinancials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SmeFinancialsRepository extends JpaRepository<SmeMonthlyFinancials, UUID> {
}

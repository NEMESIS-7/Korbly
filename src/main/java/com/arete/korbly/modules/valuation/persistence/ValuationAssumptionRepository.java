package com.arete.korbly.modules.valuation.persistence;

import com.arete.korbly.modules.valuation.domain.ValuationAssumption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ValuationAssumptionRepository extends JpaRepository<ValuationAssumption, UUID> {
}

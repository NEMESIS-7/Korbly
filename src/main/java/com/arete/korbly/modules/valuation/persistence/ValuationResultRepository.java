package com.arete.korbly.modules.valuation.persistence;

import com.arete.korbly.modules.valuation.domain.ValuationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ValuationResultRepository extends JpaRepository<ValuationResult, UUID> {

    List<ValuationResult> findByValuationAssumptionIdOrderByComputedAtDesc(UUID assumptionId);
}

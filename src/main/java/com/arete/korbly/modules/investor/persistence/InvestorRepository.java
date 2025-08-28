package com.arete.korbly.modules.investor.persistence;

import com.arete.korbly.modules.investor.domain.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, UUID> {
}

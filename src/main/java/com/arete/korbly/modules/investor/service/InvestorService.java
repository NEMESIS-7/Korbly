package com.arete.korbly.modules.investor.service;

import com.arete.korbly.modules.investor.dto.InvestorPositionDTO;
import com.arete.korbly.modules.investor.dto.PortfolioSummaryDTO;
import com.arete.korbly.modules.investor.persistence.InvestorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InvestorService {
    private final InvestorRepository investorRepository;

    public InvestorService(InvestorRepository investorRepository) {
        this.investorRepository = investorRepository;
    }

    public PortfolioSummaryDTO getSummary(UUID investorId) {
        return investorRepository.getPortfolioSummary(investorId);
    }

    public List<InvestorPositionDTO> getPositions(UUID investorId) {
        return investorRepository.positionsSimple(investorId);
    }
}

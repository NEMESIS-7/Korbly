package com.arete.korbly.modules.investor.service;

import com.arete.korbly.modules.investor.dto.InvestorPositionDTO;
import com.arete.korbly.modules.investor.dto.PortfolioSummaryDTO;
import com.arete.korbly.modules.investor.persistence.InvestorRepository;
import org.springframework.stereotype.Service;

@Service
public class InvestorService {
    private final InvestorRepository investorRepository;

    public InvestorService(InvestorRepository investorRepository) {
        this.investorRepository = investorRepository;
    }

    public PortfolioSummaryDTO getSummary(java.util.UUID investorId) {
        return investorRepository.getPortfolioSummary(investorId);
    }

    public java.util.List<InvestorPositionDTO> getPositions(java.util.UUID investorId) {
        return investorRepository.positionsSimple(investorId);
    }
}

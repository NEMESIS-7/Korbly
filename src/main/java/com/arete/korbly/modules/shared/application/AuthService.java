package com.arete.korbly.modules.shared.application;

import com.arete.korbly.infrastructure.security.JWTService;
import com.arete.korbly.modules.shared.dto.InvestorApplicationDTO;
import com.arete.korbly.modules.shared.dto.InvestorDTO;
import com.arete.korbly.modules.shared.persistence.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final JWTService jwtService;

    public AuthService(AppUserRepository appUserRepository,
                       JWTService jwtService) {
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
    }

    public InvestorDTO onboardInvestor(InvestorApplicationDTO investorApplicationDTO,
                                       MultipartFile certOfIncorporation,
                                       MultipartFile latestAuditedFinancialStatements,
                                       MultipartFile investmentPolicyStatement,
                                       MultipartFile boardResolution){
        return null;
    }

}

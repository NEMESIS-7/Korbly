package com.arete.korbly.modules.investor.web;


import com.arete.korbly.infrastructure.security.JWTService;
import com.arete.korbly.modules.investor.domain.Investor;
import com.arete.korbly.modules.investor.persistence.InvestorRepository;
import com.arete.korbly.modules.investor.service.InvestorService;
import com.arete.korbly.modules.shared.exceptions.InvestorNotFound;
import com.arete.korbly.modules.shared.persistence.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/investor")
public class InvestorController {

    private final InvestorService investorService;
    private final JWTService jwtService;
    private final AppUserRepository appUserRepository;
    private final InvestorRepository investorRepository;
    private final HttpServletRequest httpServletRequest;

    public InvestorController(InvestorService investorService,
                              JWTService jwtService,
                              AppUserRepository appUserRepository,
                              InvestorRepository investorRepository,
                              HttpServletRequest httpServletRequest) {
        this.investorService = investorService;
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
        this.investorRepository = investorRepository;
        this.httpServletRequest = httpServletRequest;
    }

    private UUID getAppUserId(HttpServletRequest request){
        return  jwtService.extractAppUserId(request);
    }

    @GetMapping("/portfolio/summary")
    public ResponseEntity<?> summary(
    ) {
        System.out.println("auth header: " + httpServletRequest.getHeader("Authorization").substring(7));

        String token = httpServletRequest.getHeader("Authorization").substring(7);
        System.out.println("user token(investor dashboard): " + token);

        Optional<Investor> investor = investorRepository.findByAppUserUserId(jwtService.extractAppUserId(token));
        if(investor.isEmpty()){
            throw new InvestorNotFound();
        }
//        System.out.println("user ID: " + getAppUserId(httpServletRequest));
        System.out.println("investor ID: " + investor.get().getInvestorId());
        return ResponseEntity.ok(investorService.getSummary(investor.get().getInvestorId()));
    }


    @GetMapping("/portfolio/positions")
    public ResponseEntity<?> positions(
    ) {
        Optional<Investor> investor = investorRepository.findByAppUserUserId(getAppUserId(httpServletRequest));
        if(investor.isEmpty()){
            throw new InvestorNotFound();
        }
        System.out.println("user ID: " + getAppUserId(httpServletRequest));
        System.out.println("investor ID: " + investor.get().getInvestorId());
        return ResponseEntity.ok(investorService.getPositions(investor.get().getInvestorId()));
    }
}

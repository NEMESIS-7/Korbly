package com.arete.korbly.modules.investor.web;

import com.arete.korbly.modules.investor.service.InvestorService;
import com.arete.korbly.modules.shared.GetUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/investor")
public class InvestorController {

    private final InvestorService investorService;
    private final GetUser getUser;

    public InvestorController(InvestorService investorService, GetUser getUser) {
        this.investorService = investorService;
        this.getUser = getUser;
    }

    @GetMapping("/portfolio/summary")
    public ResponseEntity<?> summary() {
        UUID investorId = getUser.getCurrentAuthenticatedUserId();
        log.debug("Fetching portfolio summary for investor: {}", investorId);
        return ResponseEntity.ok(investorService.getSummary(investorId));
    }

    @GetMapping("/portfolio/positions")
    public ResponseEntity<?> positions() {
        UUID investorId = getUser.getCurrentAuthenticatedUserId();
        log.debug("Fetching portfolio positions for investor: {}", investorId);
        return ResponseEntity.ok(investorService.getPositions(investorId));
    }

/*    private UUID getInvestorIdFromToken() {
        String token = extractToken();
        UUID appUserId = jwtService.extractAppUserId(token);

        log.debug("Extracted app user ID: {}", appUserId);

        return investorRepository.findByAppUserUserId(appUserId)
                .map(Investor::getInvestorId)
                .orElseThrow(InvestorNotFound::new);
    }

    private String extractToken() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        return authHeader.substring(7);
    }*/
}
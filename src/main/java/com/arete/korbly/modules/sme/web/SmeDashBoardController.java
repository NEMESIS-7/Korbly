package com.arete.korbly.modules.sme.web;

import com.arete.korbly.infrastructure.security.JWTService;
import com.arete.korbly.modules.shared.exceptions.SMENotFound;
import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.sme.persistence.SMERepository;
import com.arete.korbly.modules.sme.service.SmeDashboardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/smes")
public class SmeDashBoardController {

    private final SmeDashboardService service;
    private final JWTService jwtService;
    private final HttpServletRequest httpServletRequest;
    private final SMERepository smeRepository;

    public SmeDashBoardController(SmeDashboardService service,
                                  JWTService jwtService,
                                  HttpServletRequest httpServletRequest,
                                  SMERepository smeRepository) {
        this.service = service;
        this.jwtService = jwtService;
        this.httpServletRequest = httpServletRequest;
        this.smeRepository = smeRepository;
    }

    private UUID getAppUserId(HttpServletRequest request){
        return  jwtService.extractAppUserId(request);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(
            @RequestParam(required = false) Integer rangeMonths
    ) {
        String authHeader = httpServletRequest.getHeader("Authorization");

        // Guard clause - this is the key fix
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("auth header: " + authHeader);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        if (token.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Empty token");
        }

        System.out.println("token: " + token);

        Optional<SME> sme = smeRepository.findByAppUserUserId(jwtService.extractAppUserId(token));

        if(sme.isEmpty()){
            throw new SMENotFound();
        }

        return ResponseEntity.ok(service.getDashboard(sme.get().getSmeId(), rangeMonths));
    }
}
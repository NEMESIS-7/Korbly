package com.arete.korbly.modules.sme.web;

import com.arete.korbly.infrastructure.security.JWTService;
import com.arete.korbly.modules.shared.exceptions.SMENotFound;
import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.sme.persistence.SMERepository;
import com.arete.korbly.modules.sme.service.SmeDashboardService;
import jakarta.servlet.http.HttpServletRequest;
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
        System.out.println("request headers: " + httpServletRequest.getHeaderNames().toString());
        System.out.println("auth header in dashboard controller: " + httpServletRequest.getHeader("Authorization"));

        System.out.println("first userId: " + getAppUserId(httpServletRequest));
        Optional<SME> sme = smeRepository.findByAppUserUserId(getAppUserId(httpServletRequest));
        System.out.println("user ID in sme dashboard controller: " + getAppUserId(httpServletRequest));
        if(sme.isEmpty()){
            throw new SMENotFound();
        }
        System.out.println("sme ID: " + sme.get().getAppUser().getUserId());
        return ResponseEntity.ok(service.getDashboard(sme.get().getSmeId(), rangeMonths));
    }
}
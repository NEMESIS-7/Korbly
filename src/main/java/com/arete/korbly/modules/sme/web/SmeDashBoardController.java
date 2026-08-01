package com.arete.korbly.modules.sme.web;

import com.arete.korbly.modules.shared.GetUser;
import com.arete.korbly.modules.shared.exceptions.SMENotFound;
import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.sme.persistence.SMERepository;
import com.arete.korbly.modules.sme.service.SmeDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/smes")
public class SmeDashBoardController {

    private final SmeDashboardService service;
    private final SMERepository smeRepository;
    private final GetUser getUser;

    public SmeDashBoardController(SmeDashboardService service,
                                  SMERepository smeRepository, GetUser getUser) {
        this.service = service;
        this.getUser = getUser;
        this.smeRepository = smeRepository;
    }

/*    private UUID getAppUserId(HttpServletRequest request){
        return  jwtService.extractAppUserId(request);
    }*/

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(
            @RequestParam(required = false) Integer rangeMonths
    ) {
        Optional<SME> sme = smeRepository.findByAppUserUserId(getUser.getCurrentAuthenticatedUserId());

        if (sme.isEmpty()) {
            throw new SMENotFound();
        }

        return ResponseEntity.ok(service.getDashboard(sme.get().getSmeId(), rangeMonths));
    }
}
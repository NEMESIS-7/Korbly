package com.arete.korbly.modules.sme.web;

import com.arete.korbly.modules.shared.GetUser;
import com.arete.korbly.modules.shared.exceptions.SMENotFound;
import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.sme.domain.SmeMonthlyFinancials;
import com.arete.korbly.modules.sme.dto.SfmBulkUpsertDTO;
import com.arete.korbly.modules.sme.dto.SfmUpsertDTO;
import com.arete.korbly.modules.sme.persistence.SMERepository;
import com.arete.korbly.modules.sme.service.SmeFinancialMonthlyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/smes/financials/monthly")
public class SmeFinancialMonthlyController {

    private final SmeFinancialMonthlyService service;
    private final SMERepository smeRepository;
    private final GetUser getUser;

    public SmeFinancialMonthlyController(SmeFinancialMonthlyService service,
                                         SMERepository smeRepository,
                                          GetUser getUser) {
        this.service = service;
        this.getUser = getUser;
        this.smeRepository = smeRepository;
    }

/*    private UUID getAppUserId(HttpServletRequest request){
        return  jwtService.extractAppUserId(request);
    }*/

    @PostMapping("/upsert")
    public ResponseEntity<?> upsert(
            @RequestBody SfmUpsertDTO dto
    ) {
        Optional<SME> sme = smeRepository.findByAppUserUserId(getUser.getCurrentAuthenticatedUserId());
        log.debug("user ID: {}", getUser.getCurrentAuthenticatedUserId());
        if(sme.isEmpty()){
            throw new SMENotFound();
        }
        SmeMonthlyFinancials saved = service.upsert(dto, sme.get().getSmeId());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("/bulk-upsert")
    public ResponseEntity<?> bulk(
            @RequestBody SfmBulkUpsertDTO dto
    ) {
        Optional<SME> sme = smeRepository.findByAppUserUserId(getUser.getCurrentAuthenticatedUserId());
        log.debug("user ID: {}", getUser.getCurrentAuthenticatedUserId());
        if(sme.isEmpty()){
            throw new SMENotFound();
        }
        return new ResponseEntity<>(service.bulkUpsert(dto, sme.get().getSmeId()), HttpStatus.OK);
    }


    @GetMapping("/series")
    public ResponseEntity<?> series(
            @RequestParam(required = false) Integer months
    ) {
        Optional<SME> sme = smeRepository.findByAppUserUserId(getUser.getCurrentAuthenticatedUserId());
        log.debug("user ID: {}", getUser.getCurrentAuthenticatedUserId());
        if(sme.isEmpty()){
            throw new SMENotFound();
        }
        return new ResponseEntity<>(service.series(sme.get().getSmeId(), months), HttpStatus.OK);
    }
}

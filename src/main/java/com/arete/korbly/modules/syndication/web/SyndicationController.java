package com.arete.korbly.modules.syndication.web;

import com.arete.korbly.infrastructure.security.JWTService;
import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.exceptions.UserNotFound;
import com.arete.korbly.modules.shared.persistence.AppUserRepository;
import com.arete.korbly.modules.syndication.dto.DealDTO;
import com.arete.korbly.modules.syndication.dto.TrancheDTO;
import com.arete.korbly.modules.syndication.service.SyndicationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/syndication")
public class SyndicationController {
    private final SyndicationService syndicationService;
    private final AppUserRepository appUserRepository;
    private final JWTService jwtService;
    private final HttpServletRequest httpServletRequest;

    public SyndicationController(SyndicationService syndicationService,
                                 AppUserRepository appUserRepository,
                                 JWTService jwtService,
                                 HttpServletRequest httpServletRequest) {
        this.syndicationService = syndicationService;
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
        this.httpServletRequest = httpServletRequest;
    }

    @PostMapping("/create-deal")
    public ResponseEntity<?> createDeal(@Valid @RequestBody DealDTO dealDTO){
        UUID createdById = jwtService.extractAppUserId(httpServletRequest);
        AppUser createdBy = appUserRepository.findAppUserById(createdById)
                .orElseThrow(UserNotFound::new);
        return new ResponseEntity<>(syndicationService.createDeal(dealDTO, createdBy), HttpStatus.CREATED);
    }

    @GetMapping("/get-deals")
    public Page<DealDTO> getAllDeals(Pageable pageable){
        return syndicationService.getAllDeals(pageable);
    }


    @PutMapping("/deals/next-stage/{dealId}")
    public ResponseEntity<?> moveToNextStage(@PathVariable String dealId){
        UUID deal = UUID.fromString(dealId);
        return new ResponseEntity<>(syndicationService.moveDealToNextStage(deal), HttpStatus.OK);
    }

    @GetMapping("/deals/get-deal/{dealId}")
    public ResponseEntity<?> getDeal(@PathVariable String dealId){
        UUID deal = UUID.fromString(dealId);
        return new ResponseEntity<>(syndicationService.getDealById(deal), HttpStatus.OK);
    }

    @DeleteMapping("deals/delete/{dealId}")
    public ResponseEntity<?> deleteDeal(@PathVariable UUID dealId){
        syndicationService.deleteDeal(dealId);
        return new ResponseEntity<>("Deal deleted successfully.", HttpStatus.OK);
    }

    @PostMapping("/tranche/create/{dealId}")
    public ResponseEntity<?> createTranche(
            @PathVariable String dealId,
            @Valid @RequestBody TrancheDTO trancheDTO
            ){
        UUID deal = UUID.fromString(dealId);
        UUID appUserId = jwtService.extractAppUserId(httpServletRequest);

        return new ResponseEntity<>(syndicationService.createTranche(deal,trancheDTO,appUserId), HttpStatus.OK);
    }

    @DeleteMapping("/tranche/delete/{trancheId}")
    public ResponseEntity<?> deleteTranche(@PathVariable UUID trancheId){
        syndicationService.deleteTranche(trancheId);
        return new ResponseEntity<>("Tranche deleted successfully.", HttpStatus.OK);
    }

    @GetMapping("/tranches/get-all")
    public ResponseEntity<?> getTranches(Pageable pageable){
        return new ResponseEntity<>(syndicationService.getAllTranches(pageable), HttpStatus.OK);
    }

    @GetMapping("/tranches/{smeId}/get-all")
    public ResponseEntity<?> getSMETranches(
            @PathVariable UUID smeId
    ){
        return new ResponseEntity<>(syndicationService.getSMETranches(smeId), HttpStatus.OK);
    }
}

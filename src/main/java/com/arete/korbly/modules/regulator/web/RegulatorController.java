package com.arete.korbly.modules.regulator.web;

import com.arete.korbly.modules.regulator.dto.AuditLogDTO;
import com.arete.korbly.modules.regulator.dto.CreateRegulatorDTO;
import com.arete.korbly.modules.regulator.dto.RegulatorDealViewDTO;
import com.arete.korbly.modules.regulator.enums.RegulatorStatus;
import com.arete.korbly.modules.regulator.service.RegulatorService;
import com.arete.korbly.modules.shared.GetUser;
import com.arete.korbly.modules.shared.enums.SMEIndustry;
import com.arete.korbly.modules.syndication.enums.DealStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/regulator")
public class RegulatorController {
    private final RegulatorService regulatorService;
    private final GetUser getUser;

    public RegulatorController(RegulatorService regulatorService, GetUser getUser) {
        this.regulatorService = regulatorService;
        this.getUser = getUser;
    }

//        public ResponseEntity<?> checkout() {
//        String header = request.getHeader("Authorization");
//        String token = header.substring(7);
//        UUID userId = jwtService.extractCustomerId(token);
//        System.out.println("userId from checkout: " + userId);

    /*private UUID getAppUserId(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        System.out.println("token in private method: " + token);
       return jwtService.extractAppUserId(token);
    }*/


    @PostMapping("/create")
    public ResponseEntity<?> createRegulator(@RequestBody CreateRegulatorDTO dto, HttpServletRequest request) {
        UUID adminId = getUser.getCurrentAuthenticatedUserId();
        System.out.println("userId: " + adminId);

        return new ResponseEntity<>(regulatorService.createRegulator(dto, adminId), HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> listAllRegulators(Pageable pageable) {
        return new ResponseEntity<>(regulatorService.listAllRegulators(pageable), HttpStatus.OK);
    }

    @PutMapping("/status")
    public ResponseEntity<?> updateRegulatorStatus(@RequestParam RegulatorStatus status) {
        UUID userId = getUser.getCurrentAuthenticatedUserId();
        return new ResponseEntity<>(regulatorService.updateRegulatorStatus(userId, status), HttpStatus.OK);
    }

    @GetMapping("/deals")
    public ResponseEntity<?> getDealsForRegulator(Pageable pageable) {
        UUID userId = getUser.getCurrentAuthenticatedUserId();
        return new ResponseEntity<>(regulatorService.getDealsForRegulator(userId, pageable), HttpStatus.OK);
    }

    @GetMapping("/deals/{dealId}")
    public ResponseEntity<RegulatorDealViewDTO> getDealDetail(@PathVariable UUID dealId) {
        UUID userId = getUser.getCurrentAuthenticatedUserId();
        return new ResponseEntity<>(regulatorService.getDealDetailForRegulator(userId, dealId), HttpStatus.OK);
    }

    @GetMapping("/audit-logs/{entityType}/{entityId}")
    public ResponseEntity<Page<AuditLogDTO>> getAuditLogsForEntity(@PathVariable String entityType,
                                                                   @PathVariable UUID entityId,
                                                                   Pageable pageable) {
        UUID userId = getUser.getCurrentAuthenticatedUserId();
        return new ResponseEntity<>(regulatorService.getAuditLogsForEntity(userId, entityType, entityId, pageable), HttpStatus.OK);
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<?> getAllAuditLogs(Pageable pageable) {
        UUID userId = getUser.getCurrentAuthenticatedUserId();
        return new ResponseEntity<>(regulatorService.getAllAuditLogs(userId, pageable), HttpStatus.OK);
    }

    @GetMapping("/deals/search")
    public ResponseEntity<?> searchDeals(@RequestParam(required = false)SMEIndustry sector,
                                                                  @RequestParam(required = false) DealStatus status,
                                                                  Pageable pageable) {
        UUID userId = getUser.getCurrentAuthenticatedUserId();
        return new ResponseEntity<>(regulatorService.searchDeals(userId, sector, status, pageable), HttpStatus.OK);
    }
}

package com.arete.korbly.modules.credit.web;

import com.arete.korbly.modules.credit.application.CreditEvaluationService;
import com.arete.korbly.modules.credit.dto.FinancialsDTO;
import com.arete.korbly.modules.sme.persistence.SMERepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/credit")
public class CreditEvaluationController {

    private final CreditEvaluationService creditEvaluationService;
    private final SMERepository smeRepository;

    public CreditEvaluationController(CreditEvaluationService creditEvaluationService, SMERepository smeRepository) {
        this.creditEvaluationService = creditEvaluationService;
        this.smeRepository = smeRepository;
    }

    @PostMapping("/evaluate/{smeId}")
    public ResponseEntity<?> evaluate(
            @PathVariable UUID smeId,
            @RequestBody @Valid FinancialsDTO dto
    ) {
        return new ResponseEntity<>(creditEvaluationService.evaluateAndSave(smeId, dto), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllCreditMemos(){
        return new ResponseEntity<>(
                creditEvaluationService.getAllCreditMemos(), HttpStatus.OK
        );
    }

    @DeleteMapping("/delete-memo/{smeId}")
    public ResponseEntity<?> deleteCreditMemo(
            @PathVariable UUID smeId
    ){
        creditEvaluationService.deleteCreditMemo(smeId);
        return new ResponseEntity<>(
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete-sme-memos/{smeId}")
    public ResponseEntity<?> deleteSMEMemos(
            @PathVariable UUID smeId
    ){
        creditEvaluationService.deleteSMECreditMemos(smeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get-memo/{memoId}")
    public ResponseEntity<?> getCreditMemo(
            @PathVariable UUID memoId
    ){
        return new ResponseEntity<>(
                creditEvaluationService.findCreditMemoById(memoId), HttpStatus.OK
        );
    }

    @GetMapping("/get-memos/{smeId}")
    public ResponseEntity<?> getSMEMemos(
            @PathVariable UUID smeId
    ){
        return new ResponseEntity<>(
                creditEvaluationService.findSMECreditMemos(
                        smeId
                ), HttpStatus.OK
        );
    }

}

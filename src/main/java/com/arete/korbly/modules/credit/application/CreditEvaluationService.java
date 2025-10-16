package com.arete.korbly.modules.credit.application;

import com.arete.korbly.modules.credit.enums.ESGRiskRating;
import com.arete.korbly.modules.credit.domain.CreditMemo;
import com.arete.korbly.modules.credit.dto.CreditMemoDTO;
import com.arete.korbly.modules.credit.dto.FinancialsDTO;
import com.arete.korbly.modules.credit.mapper.CreditDTOMapper;
import com.arete.korbly.modules.credit.persistence.CreditMemoRepository;
import com.arete.korbly.modules.credit.util.*;
import com.arete.korbly.modules.shared.exceptions.InvalidFinancials;
import com.arete.korbly.modules.shared.exceptions.SMENotFound;
import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.sme.persistence.SMERepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CreditEvaluationService {
    private final AltmanScoreCalculator altmanZScoreCalculator;
    private final OhlsonScoreCalculator ohlsonScoreCalculator;
    private final DscrCalculator dscrCalculator;
    private final IcrCalculator icrCalculator;
    private final CreditMemoRepository creditMemoRepository;
    private final ESGRiskMapper eSGRiskMapper;
    private final CreditDTOMapper creditDTOMapper;
    private final SMERepository smeRepository;

    public CreditEvaluationService(AltmanScoreCalculator altmanZScoreCalculator,
                                   OhlsonScoreCalculator ohlsonScoreCalculator,
                                   DscrCalculator dscrCalculator,
                                   IcrCalculator icrCalculator,
                                   CreditMemoRepository creditMemoRepository,
                                   ESGRiskMapper eSGRiskMapper,
                                   CreditDTOMapper creditDTOMapper,
                                   SMERepository smeRepository) {
        this.altmanZScoreCalculator = altmanZScoreCalculator;
        this.ohlsonScoreCalculator = ohlsonScoreCalculator;
        this.dscrCalculator = dscrCalculator;
        this.icrCalculator = icrCalculator;
        this.creditMemoRepository = creditMemoRepository;
        this.eSGRiskMapper = eSGRiskMapper;
        this.creditDTOMapper = creditDTOMapper;
        this.smeRepository = smeRepository;
    }

    public CreditMemoDTO evaluateAndSave(UUID smeId, FinancialsDTO financialsDTO) {
        try {
            SME sme = smeRepository.findById(smeId)
                    .orElseThrow(() -> new SMENotFound("SME account with ID: " + smeId + " not found."));

            BigDecimal altmanScore = altmanZScoreCalculator.calculate(financialsDTO);
            BigDecimal ohlsonScore = ohlsonScoreCalculator.calculate(financialsDTO);
            BigDecimal dscr = dscrCalculator.calculate(financialsDTO);
            BigDecimal icr = icrCalculator.calculate(financialsDTO, null);

            boolean fxMismatchFlag = detectFxMismatch(financialsDTO);
            boolean weakCoverageFlag = detectWeakCoverage(financialsDTO);
            boolean cyclicalVulnerabilityFlag = detectCyclicalVulnerability(financialsDTO);

            CreditMemo memo = CreditMemo.builder()
                    .sme(sme)
                    .altmanScore(altmanScore)
                    .ohlsonScore(ohlsonScore)
                    .dscr(dscr.doubleValue())
                    .icr(icr.doubleValue())
                    .rawFinancials(financialsDTO)
                    .evaluatedAt(Timestamp.from(Instant.now()))
                    .weakCoverageFlag(weakCoverageFlag)
                    .fxMisMatchFlag(fxMismatchFlag)
                    .cyclicalVulnerabilityFlag(cyclicalVulnerabilityFlag)
                    .esgRiskRating(determineEsgRisk(sme)) // placeholder or dynamically evaluated later
                    .build();

            return creditDTOMapper
                    .creditMemoEntityToCreditMemoDTO(creditMemoRepository.save(memo));
        } catch (NullPointerException e) {
            throw new InvalidFinancials("User entered invalid financial data.");
        }
    }

    public List<CreditMemoDTO> getAllCreditMemos(){
        return creditMemoRepository.getAllCreditMemo()
                .stream()
                .map(creditDTOMapper::creditMemoEntityToCreditMemoDTO)
                .toList();
    }

    public void deleteSMECreditMemos(UUID smeId){
        creditMemoRepository.deleteSMECreditMemos(smeId);
    }

    public void deleteCreditMemo(UUID creditMemo){
        creditMemoRepository.deleteCreditMemo(creditMemo);
    }

    public CreditMemoDTO findCreditMemoById(UUID creditMemoId){
        return creditDTOMapper
                .creditMemoEntityToCreditMemoDTO(
                        creditMemoRepository
                                .findCreditMemoById(creditMemoId));
    }

    public List<CreditMemoDTO> findSMECreditMemos(UUID smeId){
        return creditMemoRepository
                .findSmeCreditScores(smeId)
                .stream()
                .map(creditDTOMapper::creditMemoEntityToCreditMemoDTO)
                .toList();
    }



    private boolean detectWeakCoverage(FinancialsDTO dto) {
        return dto.ebit()
                .divide(dto.totalAssets(), RoundingMode.HALF_UP)
                .compareTo(new BigDecimal("0.05")) < 0;
    }

    private boolean detectFxMismatch(FinancialsDTO dto) {
        return false; // todo refine this with FX exposure data
    }

    private boolean detectCyclicalVulnerability(FinancialsDTO dto) {
        return false; // todo logic for vulnerability detection
    }

    private ESGRiskRating determineEsgRisk(SME sme) {
        return eSGRiskMapper.getRiskRating(sme.getIndustry());
    }
}

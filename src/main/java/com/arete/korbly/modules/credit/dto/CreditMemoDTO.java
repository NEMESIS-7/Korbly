package com.arete.korbly.modules.credit.dto;

import com.arete.korbly.modules.credit.enums.ESGRiskRating;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.sme.domain.SME;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record CreditMemoDTO(
        SME sme,
        BigDecimal altmanScore,
        BigDecimal ohlsonScore,
        Boolean weakCoverageFlag,
        Boolean cyclicalVulnerabilityFlag,
        ESGRiskRating esgRiskRating,
        Timestamp evaluatedAt,
        Boolean fxMisMatchFlag,
        DeleteYn deleteYn
) {}
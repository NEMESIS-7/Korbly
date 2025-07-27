package com.arete.korbly.modules.shared.dto;

import com.arete.korbly.modules.shared.enums.InstitutionType;
import com.arete.korbly.modules.shared.enums.InvestmentFocus;
import com.arete.korbly.modules.shared.enums.RiskAppetite;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record InvestorApplicationDTO(
        String institutionName,
        String primaryContactEmail,
        String registrationNumber,
        InstitutionType institutionType,
        String phoneNumber,
        LocalDate yearEstablished,
        BigDecimal assetsUnderManagement,
        String institutionalAddress,
        BigDecimal minimumInvestment,
        RiskAppetite riskAppetite,
        Set<InvestmentFocus> investmentFocusSet
) {

}

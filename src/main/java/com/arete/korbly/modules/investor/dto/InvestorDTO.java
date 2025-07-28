package com.arete.korbly.modules.investor.dto;

import com.arete.korbly.modules.shared.enums.InstitutionType;
import com.arete.korbly.modules.shared.enums.InvestmentFocus;
import com.arete.korbly.modules.shared.enums.RiskAppetite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvestorDTO {
    private UUID investorId;
    private InstitutionType institutionType;
    private Set<InvestmentFocus> investmentFocus;
    private RiskAppetite riskAppetite;
    private String phoneNumber;
    private String registrationNumber;
    private LocalDate dateEstablished;
    private String institutionalAddress;
    private BigDecimal assetsUnderManagement;
    private BigDecimal minimumInvestment;
    private String certificateOfIncorporationURL;
    private String auditedFinancialStatementsURL;
    private String investmentPolicyStatementURL;
    private String boardResolutionURL;
}
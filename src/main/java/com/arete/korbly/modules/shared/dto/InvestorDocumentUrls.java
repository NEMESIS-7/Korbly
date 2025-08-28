package com.arete.korbly.modules.shared.dto;

public record InvestorDocumentUrls(
        String certificateOfIncorporationUrl,
        String auditedFinancialStatementsUrl,
        String investmentPolicyStatementUrl,
        String boardResolutionUrl
) {}
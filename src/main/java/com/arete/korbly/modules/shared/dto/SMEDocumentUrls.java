package com.arete.korbly.modules.shared.dto;

public record SMEDocumentUrls(
        String certOfIncorporationUrl,
        String latestFinancialStatementsUrl,
        String businessPlanUrl,
        String taxClearanceCertUrl
) {}
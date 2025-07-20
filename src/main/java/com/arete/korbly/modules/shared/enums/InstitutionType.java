package com.arete.korbly.modules.shared.enums;

public enum InstitutionType {
    ASSET_MANAGEMENT("Asset Management"),
    DEVELOPMENT_FINANCE_INSTITUTION("Development Finance Institution"),
    INSURANCE_COMPANY("Insurance Company"),
    PENSION_FUND("Pension Fund"),
    REGULATORY_BODY("Regulatory Body"),
    SOVEREIGN_WEALTH_FUND("Sovereign Wealth Fund"),
    COMMERCIAL_BANK("Commercial Bank"),
    INVESTMENT_BANK("Investment Bank"),
    FAMILY_OFFICE("Family Office"),
    WEALTH_MANAGEMENT("Wealth Management");

    private final String value;

    InstitutionType(String value) {
        this.value = value;
    }
}

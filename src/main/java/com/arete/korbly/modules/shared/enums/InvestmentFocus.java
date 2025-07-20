package com.arete.korbly.modules.shared.enums;

public enum InvestmentFocus {
    AGRICULTURE("Agriculture"),
    MANUFACTURING("Manufacturing"),
    TECHNOLOGY("Technology"),
    HEALTHCARE("Healthcare"),
    ENERGY("Energy"),
    FINANCIAL_SERVICES("Financial Services"),
    REAL_ESTATE("Real Estate"),
    INFRASTRUCTURE("Infrastructure"),
    EXPORT("Export"),
    SME_GROWTH("SME Growth");

    private final String value;

    InvestmentFocus(String value){
        this.value = value;
    }
}

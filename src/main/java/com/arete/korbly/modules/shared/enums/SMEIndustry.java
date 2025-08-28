package com.arete.korbly.modules.shared.enums;

public enum SMEIndustry {
    AGRICULTURE_AGRIBUSINESS("Agriculture and Agribusiness"),
    MANUFACTURING("Manufacturing"),
    TECHNOLOGY_DIGITAL_SERVICES("Technology and Digital Services"),
    HEALTHCARE("Healthcare"),
    EDUCATION("Education"),
    RENEWABLE_ENERGY("Renewable Energy"),
    CONSTRUCTION_REAL_ESTATE("Construction and Real Estate"),
    TRANSPORT_LOGISTICS("Transport and Logistics"),
    RETAIL_TRADE("Retail and Trade"),
    FINANCIAL_SERVICES("Financial Services");

    private final String value;

    SMEIndustry(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

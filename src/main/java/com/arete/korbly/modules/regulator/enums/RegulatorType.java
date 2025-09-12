package com.arete.korbly.modules.regulator.enums;

public enum RegulatorType {
    REGULATOR("Regulator"),
    CENTRAL_BANK("Central Bank"),
    SEC("Securities and Exchange Commission"),
    INSURANCE_COMMISSION("Insurance Commission");

    final String value;

    RegulatorType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

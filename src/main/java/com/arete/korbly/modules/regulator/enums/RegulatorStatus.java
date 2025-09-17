package com.arete.korbly.modules.regulator.enums;

public enum RegulatorStatus {
    ACTIVE("Regulator is active"),
    INACTIVE("Regulator is inactive"),
    DEACTIVATED("Regulator account is deactivated");

    final String value;

    RegulatorStatus(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

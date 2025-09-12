package com.arete.korbly.modules.shared.enums;

public enum InvestorType {
    HNWI("High Net Worth Individual"),
    PENSION("Pension Fund"),
    DFI("Development Finance Institution");

    final String value;

    InvestorType(String value){
        this.value = value;
    }
}

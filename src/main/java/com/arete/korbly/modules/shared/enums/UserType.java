package com.arete.korbly.modules.shared.enums;

import lombok.Getter;

@Getter
public enum UserType {
    INVESTOR("Institutional Investor"),
    BUSINESS("Business"),
    ADMIN("Platform Administrator"),
    HNWI("High Net Worth Individual"),
    REGULATORY_AUTHORITY("Regulatory Authority"),
    INSURANCE_REINSURANCE("Insurance, Reinsurance"),
    SME("Small Medium Scale Enterprise");


    UserType(String value){

    }
}

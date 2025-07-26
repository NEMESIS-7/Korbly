package com.arete.korbly.modules.shared.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("Administrator"),
    HNWI("High Net Worth Individual"),
    INSURER("Insurer"),
    REGULATOR("Regulator");

    private final String value;

    UserRole(String value){
        this.value = value;
    }

}

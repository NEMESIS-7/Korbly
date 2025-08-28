package com.arete.korbly.modules.shared.enums;

import lombok.Getter;

@Getter
public enum UserType {
    INVESTOR("Investor"),
    BUSINESS("Business"),
    ADMIN("Platform Administrator");


    UserType(String value){

    }
}

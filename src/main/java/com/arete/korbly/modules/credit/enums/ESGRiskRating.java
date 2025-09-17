package com.arete.korbly.modules.credit.enums;

public enum ESGRiskRating {
    LOW("Low"),
    LOW_MODERATE("Low Moderate"),
    MEDIUM("Medium"),
    HIGH("High"),
    UNKNOWN("Unknown");


    private final String value;

    ESGRiskRating(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}

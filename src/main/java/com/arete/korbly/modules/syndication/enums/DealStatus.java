package com.arete.korbly.modules.syndication.enums;

public enum DealStatus {

    DRAFT("Draft"),
    OPEN("Open"),
    CLOSED("Closed"),
    REVIEW("Pending Review"),
    PUBLISHED("Published");

    private final String value;

    DealStatus(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

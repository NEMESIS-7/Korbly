package com.arete.korbly.modules.shared.enums;

public enum SMERegion {

    GREATER_ACCRA("Greater Accra Region"),
    ASHANTI("Ashanti Region"),
    WESTERN("Western Region"),
    EASTERN("Eastern Region"),
    VOLTA("Volta Region"),
    NORTHERN("Northern Region"),
    UPPER_EAST("Upper East Region"),
    UPPER_WEST("Upper West Region"),
    BRONG_AHAFO("Brong Ahafo Region");


    private final String value;

    SMERegion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

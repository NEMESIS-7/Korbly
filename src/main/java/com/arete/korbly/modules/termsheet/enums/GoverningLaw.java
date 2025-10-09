package com.arete.korbly.modules.termsheet.enums;

public enum GoverningLaw {
    GHANA("Ghanaian Law");

    private final String value;

    GoverningLaw(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

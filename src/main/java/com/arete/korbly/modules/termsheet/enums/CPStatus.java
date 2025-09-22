package com.arete.korbly.modules.termsheet.enums;

public enum CPStatus {
    PENDING("Status is pending"),
    RECEIVED("CP received"),
    APPROVED("CP has been approved"),
    WAIVED("CP has been waived");

    private final String value;

    CPStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package com.arete.korbly.modules.termsheet.enums;

public enum TermSheetStatus {
    DRAFT("Sheet has been drafted"),
    EXECUTED("Term sheet has been executed"),
    AMENDED("Term sheet has been amended"),
    TERMINATED("Term sheet has been terminated");

    private final String value;

    public String getValue() {
        return value;
    }

    TermSheetStatus(String value) {
        this.value = value;
    }
}

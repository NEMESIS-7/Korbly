package com.arete.korbly.modules.syndication.enums;

public enum TrancheStatus {
    OPEN("Tranche has not been allocated"),
    ALLOCATED("Tranche allocated to investor"),
    PARTIALLY_ALLOCATED("Tranche is partially allocated"),
    FULLY_ALLOCATED("Tranche is fully allocated");

    final String value;
    TrancheStatus(String value) {
        this.value = value;
    }
}

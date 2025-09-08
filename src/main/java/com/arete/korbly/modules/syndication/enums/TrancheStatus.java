package com.arete.korbly.modules.syndication.enums;

public enum TrancheStatus {
    OPEN("Tranche has not been allocated"),
    ALLOCATED("Tranche allocated to investor");

    final String value;
    TrancheStatus(String value) {
        this.value = value;
    }
}

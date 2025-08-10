package com.arete.korbly.modules.syndication.enums;

public enum DealStatus {

    DRAFT("Deal is being drafted"),
    PUBLISHED("Deal is being published"),
    CLOSED("Deal is closed"),
    OPEN("Deal is open");

    private final String value;

    DealStatus(String value){
        this.value = value;
    }
}

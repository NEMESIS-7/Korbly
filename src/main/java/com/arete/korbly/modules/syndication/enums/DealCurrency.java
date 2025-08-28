package com.arete.korbly.modules.syndication.enums;

public enum DealCurrency {
    GHS("Ghanaian Cedi"),
    USD("United States Dollar");

    private final String value;
    DealCurrency(String value){
        this.value = value;
    }
}

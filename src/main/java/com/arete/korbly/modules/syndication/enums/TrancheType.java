package com.arete.korbly.modules.syndication.enums;

public enum TrancheType {
    SENIOR("Senior Tranche"),
    JUNIOR("Junior Tranche"),
    MEZZANINE("Mezzanine Trance");

    private final String value;

    TrancheType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

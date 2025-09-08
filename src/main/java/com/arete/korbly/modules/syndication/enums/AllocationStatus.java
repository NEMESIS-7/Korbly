package com.arete.korbly.modules.syndication.enums;

public enum AllocationStatus {
    PENDING("Allocation is pending approval"),
    CONFIRMED("Allocation confirmed"),
    CANCELLED("Allocation cancelled");

    final String value;

    AllocationStatus(String value){
        this.value = value;
    }
}

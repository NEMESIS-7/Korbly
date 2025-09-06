package com.arete.korbly.modules.syndication.exceptions;

public class InvalidAllocationAmount extends RuntimeException {
    public InvalidAllocationAmount(String message) {
        super(message);
    }

    public InvalidAllocationAmount() {
    }
}

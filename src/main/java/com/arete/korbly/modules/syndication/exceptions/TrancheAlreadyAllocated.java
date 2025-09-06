package com.arete.korbly.modules.syndication.exceptions;

public class TrancheAlreadyAllocated extends RuntimeException {
    public TrancheAlreadyAllocated(String message) {
        super(message);
    }

    public TrancheAlreadyAllocated() {
    }
}

package com.arete.korbly.modules.syndication.exceptions;

public class DealAmountExceeded extends RuntimeException {
    public DealAmountExceeded(String message) {
        super(message);
    }

    public DealAmountExceeded() {
    }
}

package com.arete.korbly.modules.syndication.exceptions;

public class InvalidDealUpdate extends RuntimeException {
    public InvalidDealUpdate(String message) {
        super(message);
    }

    public InvalidDealUpdate() {
    }
}

package com.arete.korbly.modules.shared.exceptions;

public class InvalidFinancials extends RuntimeException {
    public InvalidFinancials(String message) {
        super(message);
    }

    public InvalidFinancials() {
    }
}

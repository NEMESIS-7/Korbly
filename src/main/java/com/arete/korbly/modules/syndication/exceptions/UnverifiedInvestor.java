package com.arete.korbly.modules.syndication.exceptions;

public class UnverifiedInvestor extends RuntimeException {
    public UnverifiedInvestor(String message) {
        super(message);
    }

    public UnverifiedInvestor() {
    }
}

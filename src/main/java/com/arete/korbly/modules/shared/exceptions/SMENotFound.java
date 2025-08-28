package com.arete.korbly.modules.shared.exceptions;

public class SMENotFound extends RuntimeException {
    public SMENotFound(String message) {
        super(message);
    }

    public SMENotFound() {
    }
}

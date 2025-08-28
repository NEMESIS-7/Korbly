package com.arete.korbly.modules.shared.exceptions;

public class InvalidOTP extends RuntimeException {
    public InvalidOTP(String message) {
        super(message);
    }

    public InvalidOTP() {
    }
}

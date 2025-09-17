package com.arete.korbly.modules.shared.exceptions;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists(String message) {
        super(message);
    }

    public UserAlreadyExists() {
    }
}

package com.arete.korbly.modules.shared.exceptions;

public class UserNotFound extends RuntimeException {
    public UserNotFound(String message) {
        super(message);
    }

    public UserNotFound() {
    }
}

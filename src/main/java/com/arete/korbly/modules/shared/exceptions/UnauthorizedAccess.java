package com.arete.korbly.modules.shared.exceptions;

public class UnauthorizedAccess extends RuntimeException {
    public UnauthorizedAccess(String message) {
        super(message);
    }

    public UnauthorizedAccess() {
    }
}

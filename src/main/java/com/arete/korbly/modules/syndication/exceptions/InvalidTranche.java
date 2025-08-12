package com.arete.korbly.modules.syndication.exceptions;

public class InvalidTranche extends RuntimeException {
    public InvalidTranche(String message) {
        super(message);
    }

    public InvalidTranche() {
    }
}

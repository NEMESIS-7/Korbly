package com.arete.korbly.modules.syndication.exceptions;

public class DealNotFound extends RuntimeException {
    public DealNotFound(String message) {
        super(message);
    }

    public DealNotFound() {
    }
}

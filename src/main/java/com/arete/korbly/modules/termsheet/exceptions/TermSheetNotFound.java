package com.arete.korbly.modules.termsheet.exceptions;

public class TermSheetNotFound extends RuntimeException {
    public TermSheetNotFound(String message) {
        super(message);
    }

    public TermSheetNotFound() {
    }
}

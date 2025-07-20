package com.arete.korbly.modules.shared.exceptions;

public class InvalidEmail extends RuntimeException {
    public InvalidEmail(String message) {
        super(message);
    }

  public InvalidEmail() {
  }
}

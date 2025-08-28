package com.arete.korbly.modules.shared.exceptions;

public class InvestorNotFound extends RuntimeException {
    public InvestorNotFound(String message) {
        super(message);
    }

  public InvestorNotFound() {
  }
}

package com.arete.korbly.modules.shared.dto;

public record VerificationRequest(
        String primaryContactEmail,
        String otp
) {
}

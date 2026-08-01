package com.arete.korbly.modules.shared.dto;

public record VerificationResponse(
        boolean success,
        String userType,
        String userEmail

) {
}

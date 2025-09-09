package com.arete.korbly.modules.shared.dto;

public record LoginResponse(
        boolean success,
        String userRole,
        String userName,
        String email,
        String token
) {
}

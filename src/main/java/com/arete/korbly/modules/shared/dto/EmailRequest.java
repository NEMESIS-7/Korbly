package com.arete.korbly.modules.shared.dto;

public record EmailRequest(
        String recipient,
        String subject
) {
}
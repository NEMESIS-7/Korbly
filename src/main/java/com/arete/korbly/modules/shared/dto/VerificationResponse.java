package com.arete.korbly.modules.shared.dto;

import com.arete.korbly.modules.shared.enums.UserType;

public record VerificationResponse(
        boolean success,
        UserType userType

) {
}

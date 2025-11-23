package com.arete.korbly.modules.shared;

import com.arete.korbly.infrastructure.security.UserPrincipal;
import com.arete.korbly.modules.shared.domain.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetUser {

    /**
     * Retrieve the currently authenticated AppUser.
     * Throws SecurityException if unauthenticated or invalid.
     */
    public AppUser getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        validateAuthentication(authentication, UserPrincipal.class);

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        AppUser user = principal.getAppUser();

        if (user == null) {
            throw new SecurityException("Authenticated user not found or invalid principal");
        }

        return user;
    }


    /**
     * Retrieve the UUID of the current authenticated AppUser.
     */
    public UUID getCurrentAuthenticatedUserId() {
        return getCurrentAuthenticatedUser().getUserId();
    }

    /**
     * Validate authentication presence and expected principal type.
     */
    private void validateAuthentication(Authentication authentication, Class<?> expectedPrincipalClass) {
        if (authentication == null || !authentication.isAuthenticated()) {
            SecurityContextHolder.clearContext();
            throw new SecurityException("No authenticated session found");
        }

        Object principal = authentication.getPrincipal();
        if (!expectedPrincipalClass.isInstance(principal)) {
            SecurityContextHolder.clearContext();
            throw new SecurityException("Unexpected authentication principal type: " + principal.getClass().getSimpleName());
        }
    }
}

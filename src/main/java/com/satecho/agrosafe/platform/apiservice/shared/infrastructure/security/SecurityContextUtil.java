package com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityContextUtil {

    private SecurityContextUtil() {
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            try {
                return (Long) userDetails.getClass().getMethod("getUserId").invoke(userDetails);
            } catch (Exception e) {
                try {
                    return Long.parseLong(userDetails.getUsername());
                } catch (NumberFormatException ex) {
                    throw new IllegalStateException("Cannot extract user ID from principal");
                }
            }
        }

        if (principal instanceof Long userId) {
            return userId;
        }

        if (principal instanceof String username) {
            try {
                return Long.parseLong(username);
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Cannot extract user ID from principal: " + username);
            }
        }

        throw new IllegalStateException(
                "Unexpected principal type: " + (principal != null ? principal.getClass().getName() : "null"));
    }

    /**
     * True when the current authenticated principal holds ROLE_ADMIN, which is
     * allowed to bypass per-resource ownership checks (fleet administrators,
     * platform admins).
     */
    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return false;
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}

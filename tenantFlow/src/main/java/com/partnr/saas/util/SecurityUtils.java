package com.partnr.saas.util;

import com.partnr.saas.config.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {

    public static CustomUserDetails currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) {
            return null;
        }
        return (CustomUserDetails) auth.getPrincipal();
    }

    public static UUID currentTenantId() {
        CustomUserDetails user = currentUser();
        return user == null ? null : user.getTenantId();
    }

    public static UUID currentUserId() {
        CustomUserDetails user = currentUser();
        return user == null ? null : user.getUserId();
    }

    public static boolean isSuperAdmin() {
        CustomUserDetails user = currentUser();
        return user != null &&
                user.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));
    }

    public static boolean isTenantAdmin() {
        CustomUserDetails user = currentUser();
        return user != null &&
                user.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_TENANT_ADMIN"));
    }
}

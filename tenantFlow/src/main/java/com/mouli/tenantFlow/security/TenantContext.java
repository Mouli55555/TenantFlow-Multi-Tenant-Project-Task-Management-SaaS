package com.mouli.tenantFlow.security;

import java.util.UUID;

public class TenantContext {

    private static final ThreadLocal<String> TENANT = new ThreadLocal<>();
    private static final ThreadLocal<String> ROLE = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        TENANT.set(tenantId);
    }

    public static UUID getTenantId() {
        return TENANT.get();
    }

    public static void setRole(String role) {
        ROLE.set(role);
    }

    public static String getRole() {
        return ROLE.get();
    }

    public static void clear() {
        TENANT.remove();
        ROLE.remove();
    }
}

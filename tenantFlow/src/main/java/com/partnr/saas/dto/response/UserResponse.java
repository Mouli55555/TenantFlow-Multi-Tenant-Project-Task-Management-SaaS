package com.partnr.saas.dto.response;

import java.util.UUID;

public class UserResponse {

    private UUID id;
    private String email;
    private String fullName;
    private String role;
    private UUID tenantId;
    private boolean active;

    public UserResponse() {}

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final UserResponse r = new UserResponse();
        public Builder id(UUID id) { r.setId(id); return this; }
        public Builder email(String email) { r.setEmail(email); return this; }
        public Builder fullName(String fullName) { r.setFullName(fullName); return this; }
        public Builder role(String role) { r.setRole(role); return this; }
        public Builder tenantId(UUID tenantId) { r.setTenantId(tenantId); return this; }
        public Builder active(boolean active) { r.setActive(active); return this; }
        public UserResponse build() { return r; }
    }
}

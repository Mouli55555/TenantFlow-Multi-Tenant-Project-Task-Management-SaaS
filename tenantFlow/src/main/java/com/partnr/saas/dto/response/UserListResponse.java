package com.partnr.saas.dto.response;

import java.time.Instant;
import java.util.UUID;

public class UserListResponse {

    private UUID id;
    private String email;
    private String fullName;
    private String role;
    private boolean active;
    private Instant createdAt;

    public UserListResponse(
            UUID id,
            String email,
            String fullName,
            String role,
            boolean active,
            Instant createdAt
    ) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

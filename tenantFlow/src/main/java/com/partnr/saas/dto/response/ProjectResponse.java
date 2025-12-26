package com.partnr.saas.dto.response;

import java.time.Instant;
import java.util.UUID;

public class ProjectResponse {

    private UUID id;
    private String name;
    private String description;
    private String status;
    private Instant createdAt;

    public ProjectResponse(
            UUID id,
            String name,
            String description,
            String status,
            Instant createdAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

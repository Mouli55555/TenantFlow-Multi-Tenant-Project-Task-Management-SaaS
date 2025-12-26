package com.partnr.saas.entity;

import com.partnr.saas.enums.ProjectStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "projects", indexes = {
        @Index(columnList = "tenant_id")
})
public class Project {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public Project() {}

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ProjectStatus getStatus() { return status; }
    public void setStatus(ProjectStatus status) { this.status = status; }

    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Project p = new Project();
        public Builder id(UUID id) { p.setId(id); return this; }
        public Builder tenantId(UUID tenantId) { p.setTenantId(tenantId); return this; }
        public Builder name(String name) { p.setName(name); return this; }
        public Builder description(String description) { p.setDescription(description); return this; }
        public Builder status(ProjectStatus status) { p.setStatus(status); return this; }
        public Builder createdBy(UUID createdBy) { p.setCreatedBy(createdBy); return this; }
        public Builder createdAt(Instant createdAt) { p.setCreatedAt(createdAt); return this; }
        public Builder updatedAt(Instant updatedAt) { p.setUpdatedAt(updatedAt); return this; }
        public Project build() { return p; }
    }

    @PrePersist
    void onCreate() { if (createdAt == null) createdAt = Instant.now(); }

    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }
}

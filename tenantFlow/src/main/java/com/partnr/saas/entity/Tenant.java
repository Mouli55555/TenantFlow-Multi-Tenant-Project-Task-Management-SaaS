package com.partnr.saas.entity;

import com.partnr.saas.enums.SubscriptionPlan;
import com.partnr.saas.enums.TenantStatus;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tenants")
public class Tenant {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String subdomain;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenantStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_plan", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "max_users", nullable = false)
    private Integer maxUsers;

    @Column(name = "max_projects", nullable = false)
    private Integer maxProjects;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public Tenant() {}

    public Tenant(UUID id, String name, String subdomain, TenantStatus status,
                  SubscriptionPlan subscriptionPlan, Integer maxUsers, Integer maxProjects,
                  Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.subdomain = subdomain;
        this.status = status;
        this.subscriptionPlan = subscriptionPlan;
        this.maxUsers = maxUsers;
        this.maxProjects = maxProjects;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters & setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSubdomain() { return subdomain; }
    public void setSubdomain(String subdomain) { this.subdomain = subdomain; }

    public TenantStatus getStatus() { return status; }
    public void setStatus(TenantStatus status) { this.status = status; }

    public SubscriptionPlan getSubscriptionPlan() { return subscriptionPlan; }
    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; }

    public Integer getMaxUsers() { return maxUsers; }
    public void setMaxUsers(Integer maxUsers) { this.maxUsers = maxUsers; }

    public Integer getMaxProjects() { return maxProjects; }
    public void setMaxProjects(Integer maxProjects) { this.maxProjects = maxProjects; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Manual builder helper (to match previous code style: Tenant.builder()...)
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Tenant t = new Tenant();

        public Builder id(UUID id) { t.setId(id); return this; }
        public Builder name(String name) { t.setName(name); return this; }
        public Builder subdomain(String subdomain) { t.setSubdomain(subdomain); return this; }
        public Builder status(TenantStatus status) { t.setStatus(status); return this; }
        public Builder subscriptionPlan(SubscriptionPlan plan) { t.setSubscriptionPlan(plan); return this; }
        public Builder maxUsers(Integer maxUsers) { t.setMaxUsers(maxUsers); return this; }
        public Builder maxProjects(Integer maxProjects) { t.setMaxProjects(maxProjects); return this; }
        public Builder createdAt(Instant createdAt) { t.setCreatedAt(createdAt); return this; }
        public Builder updatedAt(Instant updatedAt) { t.setUpdatedAt(updatedAt); return this; }

        public Tenant build() { return t; }
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }
}

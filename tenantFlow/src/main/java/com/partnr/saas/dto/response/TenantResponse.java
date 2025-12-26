package com.partnr.saas.dto.response;

import java.util.UUID;

public class TenantResponse {

    private UUID id;
    private String name;
    private String subdomain;
    private String subscriptionPlan;
    private Integer maxUsers;
    private Integer maxProjects;

    public TenantResponse() {}

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSubdomain() { return subdomain; }
    public void setSubdomain(String subdomain) { this.subdomain = subdomain; }

    public String getSubscriptionPlan() { return subscriptionPlan; }
    public void setSubscriptionPlan(String subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; }

    public Integer getMaxUsers() { return maxUsers; }
    public void setMaxUsers(Integer maxUsers) { this.maxUsers = maxUsers; }

    public Integer getMaxProjects() { return maxProjects; }
    public void setMaxProjects(Integer maxProjects) { this.maxProjects = maxProjects; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final TenantResponse t = new TenantResponse();
        public Builder id(UUID id) { t.setId(id); return this; }
        public Builder name(String name) { t.setName(name); return this; }
        public Builder subdomain(String subdomain) { t.setSubdomain(subdomain); return this; }
        public Builder subscriptionPlan(String sp) { t.setSubscriptionPlan(sp); return this; }
        public Builder maxUsers(Integer mu) { t.setMaxUsers(mu); return this; }
        public Builder maxProjects(Integer mp) { t.setMaxProjects(mp); return this; }
        public TenantResponse build() { return t; }
    }
}

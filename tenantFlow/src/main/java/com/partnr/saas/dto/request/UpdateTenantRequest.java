package com.partnr.saas.dto.request;

import com.partnr.saas.enums.SubscriptionPlan;
import com.partnr.saas.enums.TenantStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UpdateTenantRequest {

    @Size(min = 1, max = 255)
    private String name;

    private TenantStatus status;
    private SubscriptionPlan subscriptionPlan;

    @Min(1)
    private Integer maxUsers;

    @Min(1)
    private Integer maxProjects;

    public UpdateTenantRequest() {}

    public String getName() {
        return name;
    }

    public TenantStatus getStatus() {
        return status;
    }

    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public Integer getMaxProjects() {
        return maxProjects;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(TenantStatus status) {
        this.status = status;
    }

    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public void setMaxProjects(Integer maxProjects) {
        this.maxProjects = maxProjects;
    }
}

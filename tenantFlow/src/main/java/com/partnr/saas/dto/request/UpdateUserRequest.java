package com.partnr.saas.dto.request;

public class UpdateUserRequest {

    private String fullName;
    private String role;      // tenant_admin only
    private Boolean isActive; // tenant_admin only

    public UpdateUserRequest() {}

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}

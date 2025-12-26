package com.partnr.saas.dto.request;

public class LoginRequest {

    private String email;
    private String password;
    private String tenantSubdomain;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTenantSubdomain() {
        return tenantSubdomain;
    }

    public void setTenantSubdomain(String tenantSubdomain) {
        this.tenantSubdomain = tenantSubdomain;
    }
}

package com.partnr.saas.dto.request;

public class RegisterTenantRequest {

    private String tenantName;
    private String subdomain;
    private String adminEmail;
    private String adminPassword;
    private String adminFullName;

    public RegisterTenantRequest() {}

    // getters/setters
    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }

    public String getSubdomain() { return subdomain; }
    public void setSubdomain(String subdomain) { this.subdomain = subdomain; }

    public String getAdminEmail() { return adminEmail; }
    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }

    public String getAdminPassword() { return adminPassword; }
    public void setAdminPassword(String adminPassword) { this.adminPassword = adminPassword; }

    public String getAdminFullName() { return adminFullName; }
    public void setAdminFullName(String adminFullName) { this.adminFullName = adminFullName; }
}

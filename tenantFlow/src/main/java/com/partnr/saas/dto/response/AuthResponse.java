package com.partnr.saas.dto.response;

public class AuthResponse {

    private UserResponse user;
    private TenantResponse tenant;
    private String token;
    private long expiresIn;

    public AuthResponse() {}

    public UserResponse getUser() { return user; }
    public void setUser(UserResponse user) { this.user = user; }

    public TenantResponse getTenant() { return tenant; }
    public void setTenant(TenantResponse tenant) { this.tenant = tenant; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final AuthResponse a = new AuthResponse();
        public Builder user(UserResponse u) { a.setUser(u); return this; }
        public Builder tenant(TenantResponse t) { a.setTenant(t); return this; }
        public Builder token(String token) { a.setToken(token); return this; }
        public Builder expiresIn(long e) { a.setExpiresIn(e); return this; }
        public AuthResponse build() { return a; }
    }
}

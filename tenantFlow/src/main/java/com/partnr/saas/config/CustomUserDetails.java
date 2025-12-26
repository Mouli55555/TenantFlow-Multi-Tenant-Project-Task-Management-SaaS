package com.partnr.saas.config;

import com.partnr.saas.entity.User;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {

    private final UUID id;
    private final UUID tenantId;
    private final String email;
    private final String password;
    private final String role;
    private final boolean active;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.tenantId = user.getTenantId();
        this.email = user.getEmail();
        this.password = user.getPasswordHash();
        this.role = "ROLE_" + (user.getRole() != null ? user.getRole().name() : "USER");
        this.active = user.getIsActive() != null ? user.getIsActive() : true;
    }

    public UUID getUserId() { return id; }
    public UUID getTenantId() { return tenantId; }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return active; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return active; }
}

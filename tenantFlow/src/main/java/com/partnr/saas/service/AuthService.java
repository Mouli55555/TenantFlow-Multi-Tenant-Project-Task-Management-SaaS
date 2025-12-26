package com.partnr.saas.service;

import com.partnr.saas.config.JwtUtil;
import com.partnr.saas.dto.request.LoginRequest;
import com.partnr.saas.dto.request.RegisterTenantRequest;
import com.partnr.saas.dto.response.*;
import com.partnr.saas.entity.Tenant;
import com.partnr.saas.entity.User;
import com.partnr.saas.enums.*;
import com.partnr.saas.exception.ConflictException;
import com.partnr.saas.exception.NotFoundException;
import com.partnr.saas.repository.TenantRepository;
import com.partnr.saas.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(
            TenantRepository tenantRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ---------------- REGISTER TENANT ----------------

    @Transactional
    public void registerTenant(RegisterTenantRequest req) {

        if (tenantRepository.existsBySubdomain(req.getSubdomain())) {
            throw new ConflictException("Subdomain already exists");
        }

        Tenant tenant = new Tenant();
        tenant.setName(req.getTenantName());
        tenant.setSubdomain(req.getSubdomain());
        tenant.setStatus(TenantStatus.ACTIVE);
        tenant.setSubscriptionPlan(SubscriptionPlan.FREE);
        tenant.setMaxUsers(5);
        tenant.setMaxProjects(3);

        tenantRepository.saveAndFlush(tenant);

        User admin = new User();
        admin.setTenantId(tenant.getId());
        admin.setEmail(req.getAdminEmail());
        admin.setPasswordHash(passwordEncoder.encode(req.getAdminPassword()));
        admin.setFullName(req.getAdminFullName());
        admin.setRole(Role.TENANT_ADMIN);
        admin.setIsActive(true);

        userRepository.save(admin);
    }

    // ---------------- LOGIN ----------------

    public AuthResponse login(LoginRequest req) {

        Tenant tenant = tenantRepository.findBySubdomain(req.getTenantSubdomain())
                .orElseThrow(() -> new NotFoundException("Tenant not found"));

        if (tenant.getStatus() != TenantStatus.ACTIVE) {
            throw new RuntimeException("Tenant is not active");
        }

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!tenant.getId().equals(user.getTenantId())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("User is inactive");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(
                user.getId(),
                user.getTenantId(),
                user.getRole().name(),
                user.getEmail()
        );

        return AuthResponse.builder()
                .user(UserResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .role(user.getRole().name())
                        .tenantId(user.getTenantId())
                        .active(user.getIsActive())
                        .build())
                .tenant(TenantResponse.builder()
                        .id(tenant.getId())
                        .name(tenant.getName())
                        .subdomain(tenant.getSubdomain())
                        .subscriptionPlan(tenant.getSubscriptionPlan().name())
                        .maxUsers(tenant.getMaxUsers())
                        .maxProjects(tenant.getMaxProjects())
                        .build())
                .token(token)
                .expiresIn(86400)
                .build();
    }
}

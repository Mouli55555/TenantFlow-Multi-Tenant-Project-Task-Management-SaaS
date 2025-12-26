package com.partnr.saas.service;

import com.partnr.saas.dto.request.CreateUserRequest;
import com.partnr.saas.dto.request.UpdateUserRequest;
import com.partnr.saas.dto.response.UserListResponse;
import com.partnr.saas.entity.AuditLog;
import com.partnr.saas.entity.User;
import com.partnr.saas.enums.Role;
import com.partnr.saas.exception.ForbiddenException;
import com.partnr.saas.repository.AuditLogRepository;
import com.partnr.saas.repository.TenantRepository;
import com.partnr.saas.repository.UserRepository;
import com.partnr.saas.util.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            TenantRepository tenantRepository,
            AuditLogRepository auditLogRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /* ===================== CREATE USER ===================== */

    @Transactional
    public void createUser(UUID tenantId, CreateUserRequest req) {

        if (!SecurityUtils.isTenantAdmin()) {
            throw new ForbiddenException("Forbidden");
        }

        if (!tenantId.equals(SecurityUtils.currentTenantId())) {
            throw new ForbiddenException("Forbidden");
        }

        var tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        long userCount = userRepository.countByTenantId(tenantId);
        if (userCount >= tenant.getMaxUsers()) {
            throw new RuntimeException("Subscription user limit reached");
        }

        if (userRepository.findByTenantIdAndEmail(tenantId, req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Role role = Role.USER;
        if (req.getRole() != null) {
            role = Role.valueOf(req.getRole());
        }

        User user = new User();
        // DO NOT set ID manually if your entity uses @GeneratedValue
        user.setTenantId(tenantId);
        user.setEmail(req.getEmail());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setFullName(req.getFullName());
        user.setRole(role);
        user.setIsActive(true);
        // DO NOT set createdAt manually if entity has @PrePersist that sets it

        userRepository.save(user);

        log("CREATE_USER", "user", user.getId(), tenantId);
    }

    /* ===================== LIST USERS ===================== */

    public List<UserListResponse> listUsers(UUID tenantId) {

        if (!tenantId.equals(SecurityUtils.currentTenantId()) &&
                !SecurityUtils.isSuperAdmin()) {
            throw new ForbiddenException("Forbidden");
        }

        // Use repository query to fetch tenant users directly (more efficient)
        return userRepository.findByTenantId(tenantId).stream()
                .sorted(Comparator.comparing(User::getCreatedAt).reversed())
                .map(u -> new UserListResponse(
                        u.getId(),
                        u.getEmail(),
                        u.getFullName(),
                        u.getRole().name(),
                        u.getIsActive(),
                        u.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    /* ===================== UPDATE USER ===================== */

    @Transactional
    public void updateUser(UUID userId, UpdateUserRequest req) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getTenantId().equals(SecurityUtils.currentTenantId())) {
            throw new ForbiddenException("Forbidden");
        }

        boolean isSelf = userId.equals(SecurityUtils.currentUserId());

        if (!SecurityUtils.isTenantAdmin() && !isSelf) {
            throw new ForbiddenException("Forbidden");
        }

        if (req.getFullName() != null) {
            user.setFullName(req.getFullName());
        }

        if (SecurityUtils.isTenantAdmin()) {
            if (req.getRole() != null) {
                user.setRole(Role.valueOf(req.getRole()));
            }
            if (req.getIsActive() != null) {
                user.setIsActive(req.getIsActive());
            }
        }

        userRepository.save(user);

        log("UPDATE_USER", "user", user.getId(), user.getTenantId());
    }

    /* ===================== DELETE USER ===================== */

    @Transactional
    public void deleteUser(UUID userId) {

        if (!SecurityUtils.isTenantAdmin()) {
            throw new ForbiddenException("Forbidden");
        }

        if (userId.equals(SecurityUtils.currentUserId())) {
            throw new RuntimeException("Cannot delete yourself");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getTenantId().equals(SecurityUtils.currentTenantId())) {
            throw new ForbiddenException("Forbidden");
        }

        userRepository.delete(user);

        log("DELETE_USER", "user", userId, user.getTenantId());
    }

    /* ===================== AUDIT ===================== */

    private void log(String action, String type, UUID entityId, UUID tenantId) {
        AuditLog log = new AuditLog();
        log.setId(UUID.randomUUID());
        log.setAction(action);
        log.setEntityType(type);
        log.setEntityId(entityId);
        log.setTenantId(tenantId);
        log.setUserId(SecurityUtils.currentUserId());
        log.setCreatedAt(Instant.now());
        auditLogRepository.save(log);
    }
}

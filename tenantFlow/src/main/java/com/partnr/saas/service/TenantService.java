package com.partnr.saas.service;

import com.partnr.saas.dto.request.UpdateTenantRequest;
import com.partnr.saas.entity.AuditLog;
import com.partnr.saas.entity.Tenant;
import com.partnr.saas.exception.ForbiddenException;
import com.partnr.saas.repository.*;
import com.partnr.saas.util.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final AuditLogRepository auditLogRepository;

    public TenantService(
            TenantRepository tenantRepository,
            UserRepository userRepository,
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            AuditLogRepository auditLogRepository
    ) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public Map<String, Object> getTenant(UUID tenantId) {

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        if (!SecurityUtils.isSuperAdmin() &&
                !tenantId.equals(SecurityUtils.currentTenantId())) {
            throw new ForbiddenException("Forbidden");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id", tenant.getId());
        data.put("name", tenant.getName());
        data.put("subdomain", tenant.getSubdomain());
        data.put("subscriptionPlan", tenant.getSubscriptionPlan());
        data.put("maxUsers", tenant.getMaxUsers());
        data.put("maxProjects", tenant.getMaxProjects());
        data.put("totalUsers", userRepository.countByTenantId(tenantId));
        data.put("totalProjects", projectRepository.countByTenantId(tenantId));
        data.put("totalTasks", taskRepository.countByTenantId(tenantId));

        return data;
    }

    @Transactional
    public Map<String, Object> updateTenant(UUID tenantId, UpdateTenantRequest req) {

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        if (SecurityUtils.isTenantAdmin() &&
                !tenantId.equals(SecurityUtils.currentTenantId())) {
            throw new ForbiddenException("Forbidden");
        }

        if (SecurityUtils.isTenantAdmin()) {
            if (req.getStatus() != null ||
                    req.getSubscriptionPlan() != null ||
                    req.getMaxUsers() != null ||
                    req.getMaxProjects() != null) {
                throw new ForbiddenException("Forbidden");
            }
        }

        if (SecurityUtils.isTenantAdmin() && req.getName() != null) {
            tenant.setName(req.getName());
        }

        if (SecurityUtils.isSuperAdmin()) {
            if (req.getName() != null) tenant.setName(req.getName());
            if (req.getStatus() != null) tenant.setStatus(req.getStatus());
            if (req.getSubscriptionPlan() != null)
                tenant.setSubscriptionPlan(req.getSubscriptionPlan());
            if (req.getMaxUsers() != null) tenant.setMaxUsers(req.getMaxUsers());
            if (req.getMaxProjects() != null)
                tenant.setMaxProjects(req.getMaxProjects());
        }

        tenantRepository.save(tenant);

        AuditLog log = new AuditLog();
        log.setId(UUID.randomUUID());
        log.setTenantId(tenant.getId());
        log.setUserId(SecurityUtils.currentUserId());
        log.setAction("UPDATE_TENANT");
        log.setEntityType("tenant");
        log.setEntityId(tenant.getId());
        log.setCreatedAt(Instant.now());

        auditLogRepository.save(log);

        return getTenant(tenantId);
    }
}

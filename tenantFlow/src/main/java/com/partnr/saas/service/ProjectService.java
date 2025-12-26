package com.partnr.saas.service;

import com.partnr.saas.dto.request.CreateProjectRequest;
import com.partnr.saas.dto.request.UpdateProjectRequest;
import com.partnr.saas.dto.response.ProjectResponse;
import com.partnr.saas.entity.AuditLog;
import com.partnr.saas.entity.Project;
import com.partnr.saas.enums.ProjectStatus;
import com.partnr.saas.exception.*;
import com.partnr.saas.repository.*;
import com.partnr.saas.util.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TenantRepository tenantRepository;
    private final AuditLogRepository auditLogRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            TenantRepository tenantRepository,
            AuditLogRepository auditLogRepository
    ) {
        this.projectRepository = projectRepository;
        this.tenantRepository = tenantRepository;
        this.auditLogRepository = auditLogRepository;
    }

    /* ===================== CREATE PROJECT ===================== */

    @Transactional
    public void createProject(CreateProjectRequest req) {

        if (!SecurityUtils.isTenantAdmin()) {
            throw new ForbiddenException("Only tenant admin can create projects");
        }

        UUID tenantId = SecurityUtils.currentTenantId();

        var tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new NotFoundException("Tenant not found"));

        long projectCount = projectRepository.countByTenantId(tenantId);
        if (projectCount >= tenant.getMaxProjects()) {
            throw new ConflictException("Project limit reached for subscription");
        }

        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.setTenantId(tenantId);
        project.setName(req.getName());
        project.setDescription(req.getDescription());
        project.setStatus(ProjectStatus.ACTIVE);
        project.setCreatedBy(SecurityUtils.currentUserId());
        project.setCreatedAt(Instant.now());

        projectRepository.save(project);

        log("CREATE_PROJECT", project.getId(), tenantId);
    }

    /* ===================== LIST PROJECTS ===================== */

    public List<ProjectResponse> listProjects() {

        UUID tenantId = SecurityUtils.currentTenantId();

        return projectRepository.findByTenantId(tenantId).stream()
                .map(p -> new ProjectResponse(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getStatus().name(),
                        p.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    /* ===================== UPDATE PROJECT ===================== */

    @Transactional
    public void updateProject(UUID projectId, UpdateProjectRequest req) {

        UUID tenantId = SecurityUtils.currentTenantId();

        Project project = projectRepository
                .findByIdAndTenantId(projectId, tenantId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        if (!SecurityUtils.isTenantAdmin()) {
            throw new ForbiddenException("Only tenant admin can update project");
        }

        if (req.getName() != null) {
            project.setName(req.getName());
        }

        if (req.getDescription() != null) {
            project.setDescription(req.getDescription());
        }

        if (req.getStatus() != null) {
            project.setStatus(ProjectStatus.valueOf(req.getStatus()));
        }

        project.setUpdatedAt(Instant.now());
        projectRepository.save(project);

        log("UPDATE_PROJECT", projectId, tenantId);
    }

    /* ===================== DELETE PROJECT ===================== */

    @Transactional
    public void deleteProject(UUID projectId) {

        UUID tenantId = SecurityUtils.currentTenantId();

        Project project = projectRepository
                .findByIdAndTenantId(projectId, tenantId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        if (!SecurityUtils.isTenantAdmin()) {
            throw new ForbiddenException("Only tenant admin can delete project");
        }

        projectRepository.delete(project);

        log("DELETE_PROJECT", projectId, tenantId);
    }

    /* ===================== AUDIT ===================== */

    private void log(String action, UUID entityId, UUID tenantId) {
        AuditLog log = new AuditLog();
        log.setId(UUID.randomUUID());
        log.setAction(action);
        log.setEntityType("project");
        log.setEntityId(entityId);
        log.setTenantId(tenantId);
        log.setUserId(SecurityUtils.currentUserId());
        log.setCreatedAt(Instant.now());
        auditLogRepository.save(log);
    }
}

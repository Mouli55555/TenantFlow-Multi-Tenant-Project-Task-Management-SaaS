package com.mouli.tenantFlow.service;

import com.mouli.tenantFlow.dto.CreateProjectRequest;
import com.mouli.tenantFlow.entity.Project;
import com.mouli.tenantFlow.entity.Tenant;
import com.mouli.tenantFlow.repository.ProjectRepository;
import com.mouli.tenantFlow.repository.TenantRepository;
import com.mouli.tenantFlow.security.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TenantRepository tenantRepository;
    private final AuditLogService auditLogService;

    public ProjectService(ProjectRepository projectRepository,
                          TenantRepository tenantRepository,
                          AuditLogService auditLogService) {
        this.projectRepository = projectRepository;
        this.tenantRepository = tenantRepository;
        this.auditLogService = auditLogService;
    }

    public Project create(CreateProjectRequest request) {

        Tenant tenant = tenantRepository.findById(
                UUID.fromString(TenantContext.getTenantId())
        ).orElseThrow();

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setTenant(tenant);

        Project saved = projectRepository.save(project);

        auditLogService.log(
                null,
                "CREATE_PROJECT",
                "Project",
                saved.getId().toString()
        );

        return saved;
    }

    public List<Project> getAll() {

        if ("super_admin".equals(TenantContext.getRole())) {
            return projectRepository.findAll();
        }

        return projectRepository.findByTenantId(
                UUID.fromString(TenantContext.getTenantId())
        );
    }
}

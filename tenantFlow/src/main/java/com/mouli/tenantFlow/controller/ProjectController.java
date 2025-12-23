package com.mouli.tenantFlow.controller;

import com.mouli.tenantFlow.dto.CreateProjectRequest;
import com.mouli.tenantFlow.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PreAuthorize("hasAnyRole('TENANT_ADMIN','USER','SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(projectService.getAll());
    }

    @PreAuthorize("hasAnyRole('TENANT_ADMIN','SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody CreateProjectRequest request
    ) {
        return ResponseEntity.ok(projectService.create(request));
    }
}

package com.partnr.saas.controller;

import com.partnr.saas.dto.request.CreateProjectRequest;
import com.partnr.saas.dto.request.UpdateProjectRequest;
import com.partnr.saas.service.ProjectService;
import com.partnr.saas.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /* ===================== CREATE ===================== */

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createProject(
            @Valid @RequestBody CreateProjectRequest request
    ) {
        projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Project created successfully", null));
    }

    /* ===================== LIST ===================== */

    @GetMapping
    public ResponseEntity<ApiResponse<?>> listProjects() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Projects fetched",
                        projectService.listProjects()
                )
        );
    }

    /* ===================== UPDATE ===================== */

    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponse<?>> updateProject(
            @PathVariable UUID projectId,
            @RequestBody UpdateProjectRequest request
    ) {
        projectService.updateProject(projectId, request);
        return ResponseEntity.ok(
                ApiResponse.success("Project updated successfully", null)
        );
    }

    /* ===================== DELETE ===================== */

    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse<?>> deleteProject(
            @PathVariable UUID projectId
    ) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok(
                ApiResponse.success("Project deleted successfully", null)
        );
    }
}

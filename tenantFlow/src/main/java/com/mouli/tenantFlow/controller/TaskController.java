package com.mouli.tenantFlow.controller;

import com.mouli.tenantFlow.dto.CreateTaskRequest;
import com.mouli.tenantFlow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasAnyRole('TENANT_ADMIN','USER','SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody CreateTaskRequest request
    ) {
        return ResponseEntity.ok(taskService.create(request));
    }
}

package com.partnr.saas.controller;

import com.partnr.saas.dto.request.*;
import com.partnr.saas.service.TaskService;
import com.partnr.saas.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /* ===================== CREATE ===================== */

    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<ApiResponse<?>> createTask(
            @PathVariable UUID projectId,
            @Valid @RequestBody CreateTaskRequest request
    ) {
        taskService.createTask(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Task created successfully", null));
    }

    /* ===================== LIST ===================== */

    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<ApiResponse<?>> listTasks(
            @PathVariable UUID projectId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Tasks fetched",
                        taskService.listTasks(projectId)
                )
        );
    }

    /* ===================== UPDATE ===================== */

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<ApiResponse<?>> updateTask(
            @PathVariable UUID taskId,
            @RequestBody UpdateTaskRequest request
    ) {
        taskService.updateTask(taskId, request);
        return ResponseEntity.ok(
                ApiResponse.success("Task updated successfully", null)
        );
    }

    /* ===================== UPDATE STATUS ===================== */

    @PatchMapping("/tasks/{taskId}/status")
    public ResponseEntity<ApiResponse<?>> updateStatus(
            @PathVariable UUID taskId,
            @Valid @RequestBody UpdateTaskStatusRequest request
    ) {
        taskService.updateTaskStatus(taskId, request);
        return ResponseEntity.ok(
                ApiResponse.success("Task status updated", null)
        );
    }

    /* ===================== DELETE ===================== */

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<ApiResponse<?>> deleteTask(
            @PathVariable UUID taskId
    ) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok(
                ApiResponse.success("Task deleted successfully", null)
        );
    }
}

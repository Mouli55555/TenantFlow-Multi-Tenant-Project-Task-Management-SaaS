package com.partnr.saas.service;

import com.partnr.saas.dto.request.CreateTaskRequest;
import com.partnr.saas.dto.request.UpdateTaskRequest;
import com.partnr.saas.dto.request.UpdateTaskStatusRequest;
import com.partnr.saas.dto.response.TaskResponse;
import com.partnr.saas.entity.AuditLog;
import com.partnr.saas.entity.Project;
import com.partnr.saas.entity.Task;
import com.partnr.saas.enums.TaskPriority;
import com.partnr.saas.enums.TaskStatus;
import com.partnr.saas.exception.NotFoundException;
import com.partnr.saas.repository.AuditLogRepository;
import com.partnr.saas.repository.ProjectRepository;
import com.partnr.saas.repository.TaskRepository;
import com.partnr.saas.util.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final AuditLogRepository auditLogRepository;

    public TaskService(
            TaskRepository taskRepository,
            ProjectRepository projectRepository,
            AuditLogRepository auditLogRepository
    ) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.auditLogRepository = auditLogRepository;
    }

    /* ===================== CREATE TASK ===================== */

    @Transactional
    public void createTask(UUID projectId, CreateTaskRequest req) {

        UUID tenantId = SecurityUtils.currentTenantId();

        Project project = projectRepository
                .findByIdAndTenantId(projectId, tenantId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        Task task = new Task();
        task.setTenantId(tenantId);
        task.setProjectId(project.getId());
        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.valueOf(req.getPriority()));
        task.setAssignedTo(req.getAssignedTo());
        task.setDueDate(req.getDueDate());

        taskRepository.save(task);

        log("CREATE_TASK", task.getId(), tenantId);
    }

    /* ===================== LIST TASKS ===================== */

    public List<TaskResponse> listTasks(UUID projectId) {

        UUID tenantId = SecurityUtils.currentTenantId();

        return taskRepository
                .findByTenantIdAndProjectId(tenantId, projectId)
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus().name(),
                        task.getPriority().name(),
                        task.getAssignedTo(),
                        task.getDueDate(),
                        task.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    /* ===================== UPDATE TASK ===================== */

    @Transactional
    public void updateTask(UUID taskId, UpdateTaskRequest req) {

        UUID tenantId = SecurityUtils.currentTenantId();

        Task task = taskRepository
                .findByIdAndTenantId(taskId, tenantId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        if (req.getTitle() != null) {
            task.setTitle(req.getTitle());
        }

        if (req.getDescription() != null) {
            task.setDescription(req.getDescription());
        }

        if (req.getPriority() != null) {
            task.setPriority(TaskPriority.valueOf(req.getPriority()));
        }

        if (req.getAssignedTo() != null) {
            task.setAssignedTo(req.getAssignedTo());
        }

        if (req.getDueDate() != null) {
            task.setDueDate(req.getDueDate());
        }

        taskRepository.save(task);

        log("UPDATE_TASK", taskId, tenantId);
    }

    /* ===================== UPDATE TASK STATUS ===================== */

    @Transactional
    public void updateTaskStatus(UUID taskId, UpdateTaskStatusRequest req) {

        UUID tenantId = SecurityUtils.currentTenantId();

        Task task = taskRepository
                .findByIdAndTenantId(taskId, tenantId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        task.setStatus(TaskStatus.valueOf(req.getStatus()));

        taskRepository.save(task);

        log("UPDATE_TASK_STATUS", taskId, tenantId);
    }

    /* ===================== DELETE TASK ===================== */

    @Transactional
    public void deleteTask(UUID taskId) {

        UUID tenantId = SecurityUtils.currentTenantId();

        Task task = taskRepository
                .findByIdAndTenantId(taskId, tenantId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        taskRepository.delete(task);

        log("DELETE_TASK", taskId, tenantId);
    }

    /* ===================== AUDIT LOG ===================== */

    private void log(String action, UUID entityId, UUID tenantId) {

        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setEntityType("task");
        auditLog.setEntityId(entityId);
        auditLog.setTenantId(tenantId);
        auditLog.setUserId(SecurityUtils.currentUserId());

        auditLogRepository.save(auditLog);
    }
}

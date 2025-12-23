package com.mouli.tenantFlow.service;

import com.mouli.tenantFlow.dto.CreateTaskRequest;
import com.mouli.tenantFlow.entity.Project;
import com.mouli.tenantFlow.entity.Task;
import com.mouli.tenantFlow.repository.ProjectRepository;
import com.mouli.tenantFlow.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final AuditLogService auditLogService;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       AuditLogService auditLogService) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.auditLogService = auditLogService;
    }

    public Task create(CreateTaskRequest request) {

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow();

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setProject(project);

        Task saved = taskRepository.save(task);

        auditLogService.log(
                null,
                "CREATE_TASK",
                "Task",
                saved.getId().toString()
        );

        return saved;
    }
}

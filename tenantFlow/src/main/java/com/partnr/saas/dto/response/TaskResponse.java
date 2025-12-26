package com.partnr.saas.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class TaskResponse {

    private UUID id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private UUID assignedTo;
    private LocalDate dueDate;
    private Instant createdAt;

    public TaskResponse(
            UUID id,
            String title,
            String description,
            String status,
            String priority,
            UUID assignedTo,
            LocalDate dueDate,
            Instant createdAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignedTo = assignedTo;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getPriority() { return priority; }
    public UUID getAssignedTo() { return assignedTo; }
    public LocalDate getDueDate() { return dueDate; }
    public Instant getCreatedAt() { return createdAt; }
}

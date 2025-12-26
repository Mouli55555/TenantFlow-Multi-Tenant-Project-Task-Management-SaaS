package com.partnr.saas.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.UUID;

public class CreateTaskRequest {

    @NotBlank
    private String title;

    private String description;
    private String priority;   // LOW, MEDIUM, HIGH
    private UUID assignedTo;
    private LocalDate dueDate;

    public CreateTaskRequest() {}

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public UUID getAssignedTo() {
        return assignedTo;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setAssignedTo(UUID assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}

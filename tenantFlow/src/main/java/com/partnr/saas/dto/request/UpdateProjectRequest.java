package com.partnr.saas.dto.request;

public class UpdateProjectRequest {

    private String name;
    private String description;
    private String status; // ACTIVE / ARCHIVED / COMPLETED

    public UpdateProjectRequest() {}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

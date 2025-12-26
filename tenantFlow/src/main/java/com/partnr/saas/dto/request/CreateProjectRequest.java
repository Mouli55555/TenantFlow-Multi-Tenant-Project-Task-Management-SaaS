package com.partnr.saas.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateProjectRequest {

    @NotBlank
    private String name;

    private String description;

    public CreateProjectRequest() {}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

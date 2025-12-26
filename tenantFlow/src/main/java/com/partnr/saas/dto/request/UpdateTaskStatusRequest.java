package com.partnr.saas.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateTaskStatusRequest {

    @NotBlank
    private String status; // TODO, IN_PROGRESS, COMPLETED

    public UpdateTaskStatusRequest() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

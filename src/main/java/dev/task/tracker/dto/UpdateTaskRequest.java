package dev.task.tracker.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateTaskRequest {
    @NotBlank
    private String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}

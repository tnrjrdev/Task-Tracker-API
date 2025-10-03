package dev.task.tracker.dto;

import dev.task.tracker.model.Status;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {
    @NotNull
    private Status status;
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}

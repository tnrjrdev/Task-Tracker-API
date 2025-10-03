package dev.task.tracker.web;

import dev.task.tracker.dto.CreateTaskRequest;
import dev.task.tracker.dto.UpdateStatusRequest;
import dev.task.tracker.dto.UpdateTaskRequest;
import dev.task.tracker.model.Status;
import dev.task.tracker.model.Task;
import dev.task.tracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;
    public TaskController(TaskService service) { this.service = service; }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@Valid @RequestBody CreateTaskRequest body) {
        return service.create(body.getDescription());
    }


    @GetMapping
    public List<Task> list(@RequestParam(name = "status", required = false) Status status) {
        return service.list(Optional.ofNullable(status));
    }


    @PutMapping("/{id}")
    public Task update(@PathVariable int id, @Valid @RequestBody UpdateTaskRequest body) {
        return service.updateDescription(id, body.getDescription());
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }


    @PatchMapping("/{id}/status")
    public Task updateStatus(@PathVariable int id, @Valid @RequestBody UpdateStatusRequest body) {
        return service.updateStatus(id, body.getStatus());
    }


    // atalhos
    @PostMapping("/{id}/mark-in-progress")
    public Task markInProgress(@PathVariable int id) { return service.updateStatus(id, Status.valueOf("in-progress")); }


    @PostMapping("/{id}/mark-done")
    public Task markDone(@PathVariable int id) { return service.updateStatus(id, Status.DONE); }
}
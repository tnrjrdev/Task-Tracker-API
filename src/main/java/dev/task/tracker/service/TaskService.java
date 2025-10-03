package dev.task.tracker.service;


import dev.task.tracker.model.Status;
import dev.task.tracker.model.Task;
import dev.task.tracker.repo.FileTaskRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TaskService {
    private final FileTaskRepository repo;


    public TaskService(FileTaskRepository repo) { this.repo = repo; }


    public Task create(String description) { return repo.create(description); }


    public List<Task> list(Optional<Status> status) { return repo.findAll(status); }


    public Task updateDescription(int id, String desc) { return repo.updateDescription(id, desc); }


    public void delete(int id) { repo.delete(id); }


    public Task updateStatus(int id, Status status) { return repo.updateStatus(id, status); }


    public Task require(int id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Task " + id + " não encontrada"));
    }
}

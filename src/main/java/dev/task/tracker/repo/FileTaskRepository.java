package dev.task.tracker.repo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.task.tracker.model.Status;
import dev.task.tracker.model.Task;
import org.springframework.stereotype.Repository;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class FileTaskRepository {
    private final Path file = Paths.get("tasks.json");
    private final ObjectMapper om;
    private final AtomicInteger nextId = new AtomicInteger(1);


    static class Store {
        public int nextId;
        public List<Task> tasks = new ArrayList<>();
    }


    public FileTaskRepository() {
        om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ensureStore();
    }


    private synchronized void ensureStore() {
        if (!Files.exists(file)) {
            Store s = new Store();
            s.nextId = 1;
            writeStore(s);
            nextId.set(1);
        } else {
            Store s = readStore();
            nextId.set(Math.max(1, s.nextId));
        }
    }


    private Store readStore() {
        try {
            byte[] bytes = Files.readAllBytes(file);
            String json = new String(bytes, StandardCharsets.UTF_8);
            return om.readValue(json, new TypeReference<Store>(){});
        } catch (IOException e) {
            throw new RuntimeException("tasks.json corrompido: " + e.getMessage(), e);
        }
    }

    private void writeStore(Store s) {
        try {
            String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(s);
            Path tmp = file.resolveSibling(file.getFileName().toString() + ".tmp");
            Files.writeString(tmp, json + "\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao gravar tasks.json: " + e.getMessage(), e);
        }
    }


    // CRUD
    public synchronized Task create(String description) {
        Store s = readStore();
        int id = nextId.getAndIncrement();
        Instant now = Instant.now();
        Task t = new Task(id, description.trim(), Status.TODO, now, now);
        s.tasks.add(t);
        s.nextId = nextId.get();
        writeStore(s);
        return t;
    }


    public synchronized Optional<Task> findById(int id) {
        Store s = readStore();
        return s.tasks.stream().filter(t -> t.getId() == id).findFirst();
    }


    public synchronized List<Task> findAll(Optional<Status> status) {
        Store s = readStore();
        List<Task> out = new ArrayList<>(s.tasks);
        out.sort(Comparator.comparingInt(Task::getId));
        status.ifPresent(st -> out.removeIf(t -> t.getStatus() != st));
        return out;
    }

    public synchronized Task updateDescription(int id, String description) {
        Store s = readStore();
        Task t = s.tasks.stream().filter(x -> x.getId() == id).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Task " + id + " não encontrada"));
        t.setDescription(description.trim());
        t.setUpdatedAt(Instant.now());
        writeStore(s);
        return t;
    }


    public synchronized void delete(int id) {
        Store s = readStore();
        boolean removed = s.tasks.removeIf(t -> t.getId() == id);
        if (!removed) throw new NoSuchElementException("Task " + id + " não encontrada");
        writeStore(s);
    }


    public synchronized Task updateStatus(int id, Status status) {
        Store s = readStore();
        Task t = s.tasks.stream().filter(x -> x.getId() == id).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Task " + id + " não encontrada"));
        t.setStatus(status);
        t.setUpdatedAt(Instant.now());
        writeStore(s);
        return t;
    }
}
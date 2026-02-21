package com.example.To_doAPI.controller;

import com.example.To_doAPI.dto.CreateTaskRequest;
import com.example.To_doAPI.dto.TaskResponse;
import com.example.To_doAPI.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Map;

@RestController
@SuppressWarnings("unused")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/todos")
    @SuppressWarnings("unused")
    public ResponseEntity<TaskResponse> saveTask(@Valid @RequestBody CreateTaskRequest request,
                                                 Principal principal){

        String email = principal.getName();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.saveTask(request, email));
    }

    @PutMapping("/todos/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<TaskResponse> updateTask(@Valid @RequestBody CreateTaskRequest request,
                                                   @PathVariable Long id,
                                                   Principal principal){

        String email = principal.getName();

        return ResponseEntity.ok(taskService.updateTask(id, request, email));
    }

    @DeleteMapping("/todos/{id}")
    @SuppressWarnings("unused")
    public ResponseStatusException deleteTask(@PathVariable Long id,
                                              Principal principal){

        String email = principal.getName();

        return taskService.deleteTaskById(id, email);
    }

    @GetMapping("/todos")
    @SuppressWarnings("unused")
    public ResponseEntity<Map<String, Object>> listUserTasks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String title,
            Principal principal) {

        String email = principal.getName();

        return ResponseEntity.ok(
                taskService.findTasksByUser(email, page, limit, title)
        );
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long id,
            Principal principal) {

        String email = principal.getName();

        TaskResponse task =
                taskService.getTaskByIdForUser(id, email);

        return ResponseEntity.ok(task);
    }
}

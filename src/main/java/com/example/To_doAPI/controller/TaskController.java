package com.example.To_doAPI.controller;

import com.example.To_doAPI.dto.CreateTaskRequest;
import com.example.To_doAPI.dto.TaskResponse;
import com.example.To_doAPI.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
}

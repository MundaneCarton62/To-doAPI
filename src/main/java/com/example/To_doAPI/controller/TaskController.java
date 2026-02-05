package com.example.To_doAPI.controller;

import com.example.To_doAPI.dto.CreateTaskRequest;
import com.example.To_doAPI.model.Task;
import com.example.To_doAPI.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/todos")
    public ResponseEntity<Task> saveTask(@Valid @RequestBody CreateTaskRequest request,
                                         Authentication authentication){

        String email = authentication.getName();

        Task saved = taskService.saveTask(request, email);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}

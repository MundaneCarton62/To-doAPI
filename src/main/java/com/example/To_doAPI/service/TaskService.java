package com.example.To_doAPI.service;

import com.example.To_doAPI.dto.CreateTaskRequest;
import com.example.To_doAPI.dto.TaskResponse;
import com.example.To_doAPI.model.Task;
import com.example.To_doAPI.model.User;
import com.example.To_doAPI.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserService userService;

    public TaskService(TaskRepository articleRepository, UserService userService) {
        this.taskRepository = articleRepository;
        this.userService = userService;
    }

    public TaskResponse saveTask(CreateTaskRequest request, String email) {

        User user = userService.findByEmail(email);

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setUser(user);

        Task saved = taskRepository.save(task);

        return new TaskResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription()
        );
    }

    public TaskResponse updateTask(Long id, CreateTaskRequest task, String email) {

        Task found = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + id));

        User user = userService.findByEmail(email);

        if(!found.getUser().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Forbidden");
        }

        found.setTitle(task.getTitle());
        found.setDescription(task.getDescription());

        Task saved = taskRepository.save(found);


        return new TaskResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription()
        );
    }

    public ResponseStatusException deleteTaskById(Long id, String email) {

        Task found = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + id));

        User user = userService.findByEmail(email);

        if(!found.getUser().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Forbidden");
        }

        taskRepository.deleteById(id);

        return new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    public TaskResponse getTaskByIdForUser(Long id, String email) {

        User user = userService.findByEmail(email);

        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Task not found"));

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription());
    }

    public Map<String, Object> findTasksByUser(String email, int page, int limit, String title) {

        User user = userService.findByEmail(email);

        Pageable pageable = PageRequest.of(page - 1, limit);

        Page<Task> taskPage;

        if (title != null && !title.isEmpty()) {
            taskPage = taskRepository
                    .findByUserIdAndTitleContainingIgnoreCase(
                            user.getId(), title, pageable);
        } else {
            taskPage = taskRepository
                    .findByUserId(user.getId(), pageable);
        }

        List<TaskResponse> data = taskPage.getContent()
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription()))
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("page", page);
        response.put("limit", limit);
        response.put("total", taskPage.getTotalElements());

        return response;
    }
}

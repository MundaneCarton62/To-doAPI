package com.example.To_doAPI.service;

import com.example.To_doAPI.dto.CreateTaskRequest;
import com.example.To_doAPI.model.Task;
import com.example.To_doAPI.model.User;
import com.example.To_doAPI.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserService userService;

    public TaskService(TaskRepository articleRepository, UserService userService) {
        this.taskRepository = articleRepository;
        this.userService = userService;
    }

    public Task saveTask(CreateTaskRequest request, String email) {

        User user = userService.findByEmail(email);

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setUser(user);

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task) {

        Task found = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + id));

        found.setTitle(task.getTitle());
        found.setDescription(task.getDescription());

        return taskRepository.save(found);
    }

    public void deleteTaskById(Long id) {

        taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + id));

        taskRepository.deleteById(id);
    }

    public Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + id));
    }

    public Iterable<Task> findAll() {
        return taskRepository.findAll();
    }
}

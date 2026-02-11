package com.example.To_doAPI.service;

import com.example.To_doAPI.dto.CreateTaskRequest;
import com.example.To_doAPI.dto.TaskResponse;
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

        // ownership check (you’ll add this next)
        if(!found.getUser().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to update this task");
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

package com.example.To_doAPI.ServiceTest;

import com.example.To_doAPI.dto.CreateTaskRequest;
import com.example.To_doAPI.dto.TaskResponse;
import com.example.To_doAPI.model.Task;
import com.example.To_doAPI.model.User;
import com.example.To_doAPI.repository.TaskRepository;
import com.example.To_doAPI.service.TaskService;
import com.example.To_doAPI.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    TaskRepository repository;

    @Mock
    UserService userService;

    @InjectMocks
    TaskService service;

    @Test
    void shouldSaveTask(){
        when(repository.save(any(Task.class)))
                .thenAnswer(invocation -> {
                    Task t = invocation.getArgument(0);
                    t.setId(1L);
                    return t;
                });


        User user = new User(1L, "testName", "test@email.com");
        when(userService.findByEmail("test@email.com"))
                .thenReturn(user);


        CreateTaskRequest request = new CreateTaskRequest("Test task", "Test task description");
        TaskResponse result = service.saveTask(request, "test@email.com");


        assertNotNull(result.getId());
        verify(repository).save(any(Task.class));
        verify(userService).findByEmail("test@email.com");
    }

    @Test
    void shouldUpdateTask(){

        User user = new User(1L, "testName", "test@email.com");
        Task foundTask = new Task(1L,"Test task", "Test description", user);
        when(repository.findById(1L))
                .thenReturn(Optional.of(foundTask));

        when(userService.findByEmail("test@email.com"))
                .thenReturn(user);


        when(repository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateTaskRequest updatedTask = new CreateTaskRequest("Test task", "Test task description");
        TaskResponse result = service.updateTask(1L, updatedTask, "test@email.com");


        assertEquals(updatedTask.getTitle(), result.getTitle());
        assertEquals(updatedTask.getDescription(), result.getDescription());

        verify(repository).save(any(Task.class));
        verify(repository).findById(1L);
        verify(userService).findByEmail("test@email.com");
    }

    @Test
    void shouldNotUpdateTask(){

        User user = new User(1L, "testName", "test@email.com");
        Task foundTask = new Task(1L,"Test task", "Test description", user);
        when(repository.findById(1L))
                .thenReturn(Optional.of(foundTask));

        User invalidUser = new User(66L, "testName", "invalid@email.com");
        when(userService.findByEmail("invalid@email.com"))
                .thenReturn(invalidUser);

        CreateTaskRequest updatedTask = new CreateTaskRequest("Test task", "Test task description");
        assertThrows(ResponseStatusException.class, () -> {
            service.updateTask(1L, updatedTask, "invalid@email.com");
        });


        verify(repository).findById(1L);
        verify(userService).findByEmail("invalid@email.com");

        //Save should not be called
        verify(repository, never()).save(any(Task.class));
    }
    @Test
    void shouldDeleteTask() {

        User user = new User(1L, "testName", "test@email.com");
        Task foundTask = new Task(1L,"Test task", "Test description", user);
        when(repository.findById(1L))
                .thenReturn(Optional.of(foundTask));

        when(userService.findByEmail("test@email.com"))
                .thenReturn(user);

        doNothing().when(repository).deleteById(1L);

        service.deleteTaskById(1L, "test@email.com");

        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void shouldNotDeleteOtherUserTask() {

        User user = new User(1L, "testName", "test@email.com");
        Task foundTask = new Task(1L,"Test task", "Test description", user);
        when(repository.findById(1L))
                .thenReturn(Optional.of(foundTask));


        User invalidUser = new User(66L, "testName", "invalid@email.com");
        when(userService.findByEmail("test@email.com"))
                .thenReturn(invalidUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.deleteTaskById(1L, "test@email.com"));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        verify(repository).findById(1L);
        verify(repository, never()).deleteById(anyLong());

    }

    @Test
    void shouldGetTaskByIdForUser(){
        User user = new User(1L, "testName", "test@email.com");
        Task foundTask = new Task(1L,"Test task", "Test description", user);
        when(repository.findByIdAndUserId(1L, user.getId()))
                .thenReturn(Optional.of(foundTask));

        when(userService.findByEmail("test@email.com"))
                .thenReturn(user);

        TaskResponse response = service.getTaskByIdForUser(1L, "test@email.com");

        assertEquals(response.getId(), foundTask.getId());
        assertEquals(response.getTitle(), foundTask.getTitle());
        assertEquals(response.getDescription(), foundTask.getDescription());

        verify(repository).findByIdAndUserId(1L, 1L);
        verify(userService).findByEmail("test@email.com");
    }

    @Test
    void shouldNotGetTaskByIdForUser(){
        User user = new User(1L, "testName", "test@email.com");
        when(userService.findByEmail("test@email.com"))
                .thenReturn(user);

        when(repository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.empty());


        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.getTaskByIdForUser(1L, "test@email.com"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(repository).findByIdAndUserId(1L, 1L);
        verify(userService).findByEmail("test@email.com");
    }

    @Test
    void shouldFindTasksByUserWithoutTitleFilter() {

        User user = new User(1L, "testName", "test@email.com");

        when(userService.findByEmail("test@email.com"))
                .thenReturn(user);

        Task task = new Task(1L, "Test task", "Test description", user);

        Page<Task> page = new PageImpl<>(
                List.of(task),
                PageRequest.of(0, 10),
                1
        );

        when(repository.findByUserId(eq(1L), any(Pageable.class)))
                .thenReturn(page);

        Map<String, Object> result =
                service.findTasksByUser("test@email.com", 1, 10, null);

        List<TaskResponse> data = (List<TaskResponse>) result.get("data");

        assertEquals(1, data.size());
        assertEquals("Test task", data.get(0).getTitle());
        assertEquals(1, result.get("page"));
        assertEquals(10, result.get("limit"));
        assertEquals(1L, result.get("total"));

        verify(repository).findByUserId(eq(1L), any(Pageable.class));
    }

    @Test
    void shouldFindTasksByUserWithTitleFilter() {

        User user = new User(1L, "testName", "test@email.com");

        when(userService.findByEmail("test@email.com"))
                .thenReturn(user);

        Task task = new Task(1L, "Spring Boot", "Test description", user);

        Page<Task> page = new PageImpl<>(
                List.of(task),
                PageRequest.of(0, 10),
                1
        );

        when(repository
                .findByUserIdAndTitleContainingIgnoreCase(
                        eq(1L), eq("Spring"), any(Pageable.class)))
                .thenReturn(page);

        Map<String, Object> result =
                service.findTasksByUser("test@email.com", 1, 10, "Spring");

        List<TaskResponse> data = (List<TaskResponse>) result.get("data");

        assertEquals(1, data.size());
        assertEquals("Spring Boot", data.get(0).getTitle());
        assertEquals(1L, result.get("total"));

        verify(repository)
                .findByUserIdAndTitleContainingIgnoreCase(
                        eq(1L), eq("Spring"), any(Pageable.class));
    }
}

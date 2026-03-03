package com.example.To_doAPI.ControllerTest;

import com.example.To_doAPI.controller.TaskController;
import com.example.To_doAPI.dto.CreateTaskRequest;
import com.example.To_doAPI.dto.TaskResponse;
import com.example.To_doAPI.service.JwtService;
import com.example.To_doAPI.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @MockBean
    TaskService service;

    @MockBean
    @SuppressWarnings("unused")
    JwtService jwtService;


    @Autowired
    @SuppressWarnings("unused")
    private MockMvc mockMvc;


    @Test
    @WithMockUser(username = "test@email.com")
    void shouldCreateTask() throws Exception{

        TaskResponse taskResponse = new TaskResponse(1L, "Test task", "Test description");

        when(service.saveTask(any(CreateTaskRequest.class), eq("test@email.com")))
                .thenReturn(taskResponse);



        mockMvc.perform(post("/todos")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "title": "Test task",
                  "description": "Test description"
                }
            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test task"))
                .andExpect(jsonPath("$.description").value("Test description"));

        verify(service).saveTask(any(CreateTaskRequest.class), eq("test@email.com"));
    }

    @Test
    void shouldReturn401WhenCreatingTaskWithoutAuthentication() throws Exception {

        mockMvc.perform(post("/todos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
              "title": "Test task",
              "description": "Test description"
            }
            """))
                .andExpect(status().isUnauthorized());
    }



    @Test
    @WithMockUser(username = "test@email.com")
    void shouldUpdateTask() throws Exception{

        TaskResponse taskResponse = new TaskResponse(1L, "Test updated task", "Test updated description");

        when(service.updateTask(eq(1L), any(CreateTaskRequest.class), eq("test@email.com")))
                .thenReturn(taskResponse);



        mockMvc.perform(put("/todos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "title": "Test updated task",
                  "description": "Test updated description"
                }
            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test updated task"))
                .andExpect(jsonPath("$.description").value("Test updated description"));

        verify(service).updateTask(eq(1L), any(CreateTaskRequest.class), eq("test@email.com"));
    }

    @Test
    void shouldReturn401WhenUpdatingTaskWithoutAuthentication() throws Exception {

        mockMvc.perform(put("/todos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
              "title": "Updated",
              "description": "Updated"
            }
            """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@email.com")
    void shouldReturn403WhenUpdatingOtherUserTask() throws Exception {


        when(service.updateTask(eq(1L), any(CreateTaskRequest.class), eq("test@email.com")))
                .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN));

        mockMvc.perform(put("/todos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
              "title": "Updated",
              "description": "Updated"
            }
            """))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test@email.com")
    void shouldDeleteTask() throws Exception{

        mockMvc.perform(delete("/todos/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(service).deleteTaskById(eq(1L), eq("test@email.com"));
    }

    @Test
    void shouldReturn401WhenDeletingTaskWithoutAuthentication() throws Exception {

        mockMvc.perform(delete("/todos/1")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@email.com")
    void shouldReturn403WhenDeletingOtherUserTask() throws Exception {

        when(service.deleteTaskById(eq(1L), eq("test@email.com")))
                .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN));

        mockMvc.perform(delete("/todos/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test@email.com")
    void shouldListUsersTasks() throws Exception{

        Map<String, Object> response = new HashMap<>();
        response.put("data", List.of(
                new TaskResponse(1L, "Test found task", "Test found description")
        ));
        response.put("page", 1);
        response.put("limit", 10);
        response.put("total", 1L);

        when(service.findTasksByUser(eq("test@email.com"), eq(1), eq(10), eq(null)))
                .thenReturn(response);

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Test found task"))
                .andExpect(jsonPath("$.data[0].description").value("Test found description"));

        verify(service).findTasksByUser(eq("test@email.com"), eq(1), eq(10), eq(null));
    }




    @Test
    @WithMockUser(username = "test@email.com")
    void shouldFindTaskById() throws Exception{

        TaskResponse taskResponse = new TaskResponse(1L, "Test found task", "Test found description");

        when(service.getTaskByIdForUser(eq(1L), eq("test@email.com")))
                .thenReturn(taskResponse);



        mockMvc.perform(get("/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test found task"))
                .andExpect(jsonPath("$.description").value("Test found description"));

        verify(service).getTaskByIdForUser(eq(1L), eq("test@email.com"));
    }

    @Test
    void shouldReturn401WhenFindingTaskWithoutAuthentication() throws Exception {

        mockMvc.perform(get("/todos/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@email.com")
    void shouldReturn403WhenFindingOtherUserTask() throws Exception {

        when(service.getTaskByIdForUser(eq(1L), eq("test@email.com")))
                .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN));

        mockMvc.perform(get("/todos/1"))
                .andExpect(status().isForbidden());
    }
}

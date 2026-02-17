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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
}

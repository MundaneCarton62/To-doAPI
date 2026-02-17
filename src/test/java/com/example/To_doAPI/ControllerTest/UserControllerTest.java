package com.example.To_doAPI.ControllerTest;

import com.example.To_doAPI.controller.UserController;
import com.example.To_doAPI.model.User;
import com.example.To_doAPI.service.JwtService;
import com.example.To_doAPI.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @MockBean
    UserService service;

    @MockBean
    @SuppressWarnings("unused")
    JwtService jwtService;


    @Autowired
    @SuppressWarnings("unused")
    private MockMvc mockMvc;

    @Test
    void shouldSaveUser() throws Exception {

        User createdUser = new User("Test name", "test@mail.com", "mypassword");
        when(service.saveUser(any(User.class)))
                .thenReturn(createdUser);

        when(jwtService.generateToken(eq("test@mail.com")))
                .thenReturn("asdfghjkl");

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name" : "Test name",
                          "email": "test@mail.com",
                          "password": "mypassword"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("asdfghjkl"));
    }

    @Test
    void shouldNotSaveUser() throws Exception {


        mockMvc.perform(post("/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "bad" : "request"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSaveDupeUser() throws Exception {

        when(service.saveUser(any(User.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "User already exist with email" + User.class.getName()));

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name" : "Test name",
                          "email": "test@mail.com",
                          "password": "mypassword"
                        }
                        """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("User already exist with email" + User.class.getName()));
    }
}

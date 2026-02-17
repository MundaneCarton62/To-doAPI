package com.example.To_doAPI.ServiceTest;

import com.example.To_doAPI.model.User;
import com.example.To_doAPI.repository.UserRepository;
import com.example.To_doAPI.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository repository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService service;

    @Test
    void shouldSaveUser(){

        when(repository.findByEmailIgnoreCase(anyString()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(anyString()))
                .thenReturn("asasdasdasdas");


        when(repository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User u = invocation.getArgument(0);
                    u.setId(1L);
                    return u;
                });

        User user = new User(1L,"Test", "test@email.com", "password");
        User result = service.saveUser(user);

        assertNotNull(result.getId());

        verify(repository).save(any(User.class));
        verify(repository).findByEmailIgnoreCase(anyString());
    }

    @Test
    void shouldNotSaveUser(){
        when(repository.findByEmailIgnoreCase(anyString()))
                .thenReturn(Optional.of(new User(1L,"Test name", "Test email")));


        User user = new User(1L,"Test", "test@email.com", "password");
        assertThrows(ResponseStatusException.class, () -> {
            service.saveUser(user);
        });

        verify(repository).findByEmailIgnoreCase(anyString());

        //Save should not be called
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void shouldFindUser(){
        User foundUser = new User(1L,"Test name", "Test email");

        when(repository.findByEmailIgnoreCase("test@email.com"))
                .thenReturn(Optional.of(foundUser));


        User result = service.findByEmail("test@email.com");

        verify(repository).findByEmailIgnoreCase("test@email.com");

        assertEquals(foundUser.getEmail(), result.getEmail());
        assertEquals(foundUser.getName(), result.getName());
    }

    @Test
    void shouldNotFindUser(){

        when(repository.findByEmailIgnoreCase("test@email.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            service.findByEmail("test@email.com");
        });

    }
}

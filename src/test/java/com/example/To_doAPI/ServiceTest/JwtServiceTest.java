package com.example.To_doAPI.ServiceTest;

import com.example.To_doAPI.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }
    @Test
    void shouldGenerateToken() {
        String email = "test@mail.com";

        String token = jwtService.generateToken(email);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldExtractEmailFromToken() {
        String email = "test@mail.com";

        String token = jwtService.generateToken(email);
        String extractedEmail = jwtService.extractEmail(token);

        assertEquals(email, extractedEmail);
    }

    @Test
    void shouldReturnTrueForValidToken() {
        String token = jwtService.generateToken("test@mail.com");

        boolean isValid = jwtService.isTokenValid(token);

        assertTrue(isValid);
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        String invalidToken = "invalid.token.value";

        boolean isValid = jwtService.isTokenValid(invalidToken);

        assertFalse(isValid);
    }
}
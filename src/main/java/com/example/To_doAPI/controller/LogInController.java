package com.example.To_doAPI.controller;

import com.example.To_doAPI.dto.LoginRequest;
import com.example.To_doAPI.dto.LoginResponse;
import com.example.To_doAPI.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings("unused")
public class LogInController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LogInController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginPage(@Valid @RequestBody LoginRequest user){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        String token = jwtService.generateToken(user.getEmail());

        return ResponseEntity.ok(new LoginResponse(token));

    }


}

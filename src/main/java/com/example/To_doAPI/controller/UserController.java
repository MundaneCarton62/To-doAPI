package com.example.To_doAPI.controller;

import com.example.To_doAPI.dto.LoginResponse;
import com.example.To_doAPI.dto.RegisterRequest;
import com.example.To_doAPI.model.User;
import com.example.To_doAPI.service.JwtService;
import com.example.To_doAPI.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings("unused")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService){
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> saveUser(@Valid @RequestBody RegisterRequest request){

        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );

        User saved = userService.saveUser(user);

        String token = jwtService.generateToken(user.getEmail());

        return ResponseEntity.ok(new LoginResponse(token));
    }
}

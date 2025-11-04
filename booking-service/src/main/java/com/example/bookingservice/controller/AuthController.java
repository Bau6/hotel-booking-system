package com.example.bookingservice.controller;

import com.example.bookingservice.dto.AuthRequest;
import com.example.bookingservice.dto.AuthResponse;
import com.example.bookingservice.dto.RegisterRequest;
import com.example.bookingservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        String token = authService.register(request.getUsername(), request.getPassword());
        return new AuthResponse(token);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        String token = authService.authenticate(request.getUsername(), request.getPassword());
        return new AuthResponse(token);
    }
}
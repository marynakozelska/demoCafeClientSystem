package com.example.democafeclientsystem.controllers;

import com.example.democafeclientsystem.dto.AuthResponse;
import com.example.democafeclientsystem.dto.AuthenticationRequest;
import com.example.democafeclientsystem.dto.RegisterRequest;
import com.example.democafeclientsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {
    private final AuthService service;

    @PostMapping(path = "/register")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody RegisterRequest request) {

        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping(path = "/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(service.authenticate(request));
    }

}
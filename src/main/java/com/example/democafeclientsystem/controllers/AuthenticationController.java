package com.example.democafeclientsystem.controllers;

import com.example.democafeclientsystem.dto.AuthResponse;
import com.example.democafeclientsystem.dto.AuthenticationRequest;
import com.example.democafeclientsystem.dto.RegisterRequest;
import com.example.democafeclientsystem.exceptions.UserAlreadyExists;
import com.example.democafeclientsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {
    private final AuthService service;

    @PostMapping(path = "/register")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping(path = "/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(service.authenticate(request));
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<String> handleConstraintViolationException(UserAlreadyExists exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

}
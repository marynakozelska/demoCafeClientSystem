package com.example.democafeclientsystem.controllers;

import com.example.democafeclientsystem.config.JwtUtil;
import com.example.democafeclientsystem.dto.AuthResponse;
import com.example.democafeclientsystem.dto.AuthenticationRequest;
import com.example.democafeclientsystem.dto.RegisterRequest;
import com.example.democafeclientsystem.exceptions.UserAlreadyExists;
import com.example.democafeclientsystem.service.AuthService;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {
    private final AuthService service;
    private final JwtUtil jwtUtil;

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

    @PostMapping(value = "/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
        log.debug("Refreshing token...");

        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
        String email = expectedMap.get("sub").toString();
        var user = service.loadUserByEmail(email);

        var role = user.getAuthorities()
                .stream()
                .findFirst()
                .orElse(null);
        String roleStr = role.getAuthority().substring("ROLE_".length());

        String token = jwtUtil.generateRefreshToken(expectedMap, user);

        return ResponseEntity.ok(
                AuthResponse
                        .builder()
                        .token(token)
                        .role(roleStr)
                        .build()
        );
    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }

}
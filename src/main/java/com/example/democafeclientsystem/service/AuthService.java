package com.example.democafeclientsystem.service;

import com.example.democafeclientsystem.config.JwtUtil;
import com.example.democafeclientsystem.dto.AuthResponse;
import com.example.democafeclientsystem.dto.AuthenticationRequest;
import com.example.democafeclientsystem.dto.RegisterRequest;
import com.example.democafeclientsystem.enums.Role;
import com.example.democafeclientsystem.exceptions.UserAlreadyExists;
import com.example.democafeclientsystem.repositories.RoleRepository;
import com.example.democafeclientsystem.repositories.UserRepository;
import com.example.democafeclientsystem.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExists("User with this email already exists.");
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        Role userRole = roleRepository.findByName("USER");
        user.setRole(userRole);

        repository.save(user);

        var jwtToken = jwtUtil.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .role(userRole.getName())
                .build();
    }

    public AuthResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail());
        var jwtToken = jwtUtil.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .role(user.getRole().getName())
                .build();
    }
}

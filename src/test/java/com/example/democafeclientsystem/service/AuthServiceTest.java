package com.example.democafeclientsystem.service;

import com.example.democafeclientsystem.config.JwtUtil;
import com.example.democafeclientsystem.dto.AuthResponse;
import com.example.democafeclientsystem.dto.AuthenticationRequest;
import com.example.democafeclientsystem.dto.RegisterRequest;
import com.example.democafeclientsystem.entities.User;
import com.example.democafeclientsystem.enums.Role;
import com.example.democafeclientsystem.exceptions.UserAlreadyExists;
import com.example.democafeclientsystem.repositories.RoleRepository;
import com.example.democafeclientsystem.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService underTest;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;

    private static final String USER_ROLE_NAME = "USER";
    static User user;

    @BeforeAll
    static void beforeAll() {

        Role userRole = new Role();
        userRole.setId(1);
        userRole.setName("USER");

        user = User
                .builder()
                .firstName("Andriy")
                .email("andriy@gmail.com")
                .password("password")
                .role(userRole)
                .build();
    }

    @Test
    void register() {
        RegisterRequest request = new RegisterRequest(
                user.getFirstName(),
                user.getEmail(),
                user.getPassword());

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);
        when(roleRepository.findByName(USER_ROLE_NAME)).thenReturn(user.getRole());
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(jwtUtil.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthResponse response = underTest.register(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isNotNull();
        assertThat(response.getRole()).isEqualTo(user.getRole().getName());

        verify(userRepository).existsByEmail(user.getEmail());
        verify(roleRepository).findByName(USER_ROLE_NAME);
        verify(userRepository).save(any(User.class));
        verify(jwtUtil).generateToken(any(User.class));
    }

    @Test
    void registerUserAlreadyExists() {
        RegisterRequest request = new RegisterRequest(
                user.getFirstName(),
                user.getEmail(),
                user.getPassword());

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> underTest.register(request))
                .isInstanceOf(UserAlreadyExists.class)
                .hasMessageContaining("User with this email already exists.");

        verify(userRepository).existsByEmail(user.getEmail());
        verify(roleRepository, never()).findByName(any());
        verify(userRepository, never()).save(any(User.class));
        verify(jwtUtil, never()).generateToken(any(User.class));

    }

    @Test
    void authenticate() {
        AuthenticationRequest request = AuthenticationRequest
                .builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager
                .authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtUtil.generateToken(any(User.class))).thenReturn("jwtToken");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        AuthResponse authResponse = underTest.authenticate(request);

        assertThat(authResponse).isNotNull();
        assertThat(authResponse.getToken()).isNotNull();
        assertThat(authResponse.getRole()).isEqualTo(USER_ROLE_NAME);

        verify(userRepository).findByEmail(user.getEmail());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(any(User.class));
    }

    @Test
    void authenticateUserNotFound() {
        AuthenticationRequest request = AuthenticationRequest
                .builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        when(authenticationManager
                .authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(UsernameNotFoundException.class);

        assertThatThrownBy(() -> underTest
                .authenticate(request))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
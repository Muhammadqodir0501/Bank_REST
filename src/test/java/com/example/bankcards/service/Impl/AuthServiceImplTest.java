package com.example.bankcards.service.Impl;

import com.example.bankcards.dto.request.LoginRequest;
import com.example.bankcards.dto.request.RegisterRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtService;
import com.example.bankcards.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_shouldSucceedForNewUser() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("newUser");
        request.setPassword("password123");
        request.setFullName("New User");


        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        String result = authService.register(request);

        assertEquals("User registered successfully as USER", result);
    }


    @Test
    void register_shouldFailIfUsernameExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existingUser");

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));
        String result = authService.register(request);
        assertEquals("Username is already in use", result);
    }

    @Test
    void login_shouldReturnTokenForValidCredential() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        when(jwtService.generateToken("testuser")).thenReturn("test-token");

        String token = authService.login(request);

        assertEquals("test-token", token);
    }


    }

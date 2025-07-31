package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.LoginRequest;
import com.example.bankcards.dto.request.RegisterRequest;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.UserStatus;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtService;
import com.example.bankcards.service.abstractions.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public String register(RegisterRequest dto){
        if(userRepository.findByUsername(dto.getUsername()).isPresent()){
            return "Username is already in use";
        }

        Role role = dto.getUsername().equals("admin") ? Role.ADMIN : Role.USER;

        User user = User.builder()
                .username(dto.getUsername())
                .fullName(dto.getFullName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        return "User registered successfully as " + role;
    }
    @Override
    public String login(LoginRequest dto){
        var authToken = new UsernamePasswordAuthenticationToken(
                dto.getUsername(),
                dto.getPassword());
        authenticationManager.authenticate(authToken);

        return jwtService.generateToken(dto.getUsername());
    }
}

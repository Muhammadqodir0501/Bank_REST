package com.example.bankcards.service.abstractions;

import com.example.bankcards.dto.request.LoginRequest;
import com.example.bankcards.dto.request.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest dto);

    String login(LoginRequest dto);
}

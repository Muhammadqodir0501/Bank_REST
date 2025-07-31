package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters long")
    private String username;

    @NotBlank(message = "Full name cannot be empty")
    @Size(max = 20, message = "Full name must not exceed 20 characters")
    private String fullName;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;
}

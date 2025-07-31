package com.example.bankcards.dto.response;

import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.entity.enums.UserStatus;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String fullName;
    private UserStatus status;
    private Role role;
}

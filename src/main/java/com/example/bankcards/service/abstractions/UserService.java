package com.example.bankcards.service.abstractions;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.enums.UserStatus;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<UserResponse> findAllUsers(int page, int size);
    UserResponse changeUserStatus(Long userId, UserStatus newStatus);

}

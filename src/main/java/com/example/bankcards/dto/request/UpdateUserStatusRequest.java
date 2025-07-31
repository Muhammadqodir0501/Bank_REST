package com.example.bankcards.dto.request;

import com.example.bankcards.entity.enums.UserStatus;
import lombok.Data;

@Data
public class UpdateUserStatusRequest {
    private UserStatus status;
}

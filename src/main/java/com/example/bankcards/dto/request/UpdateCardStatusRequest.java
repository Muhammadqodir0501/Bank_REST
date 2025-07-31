package com.example.bankcards.dto.request;

import com.example.bankcards.entity.enums.CardStatus;
import lombok.Data;

@Data
public class UpdateCardStatusRequest {
    private CardStatus status;
}

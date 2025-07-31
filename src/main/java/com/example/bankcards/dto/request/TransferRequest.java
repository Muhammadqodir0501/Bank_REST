package com.example.bankcards.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    @NotNull(message = "Sender card ID cannot be null")
    @Positive(message = "Sender card ID must be a positive number")
    private Long fromCardId;

    @NotNull(message = "Receiver card ID cannot be null")
    @Positive(message = "Receiver card ID must be a positive number")
    private Long toCardId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Transfer amount must be positive")
    private BigDecimal amount;
}

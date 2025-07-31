package com.example.bankcards.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CardRequest {

    @NotBlank(message = "Card number cannot be blank")
    @Pattern(regexp = "^\\d{16}$", message = "Card number must be exactly 16 digits")
    private String number;

    @NotBlank(message = "Owner name cannot be blank")
    @Size(max = 20, message = "Owner name must not exceed 20 characters")
    private String ownerName;

    @NotNull(message = "Balance cannot be null")
    @PositiveOrZero(message = "Balance must be zero or positive")
    private BigDecimal balance;

    @NotNull(message = "Expiration date cannot be null")
    @Future(message = "Expiration date must be in the future")
    private LocalDate expirationDate;

    @NotNull(message = "Owner ID cannot be null")
    @Positive(message = "Owner ID must be a positive number")
    private Long ownerId;

}

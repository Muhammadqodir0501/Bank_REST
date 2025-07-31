package com.example.bankcards.dto.response;

import com.example.bankcards.entity.enums.CardStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CardResponse {
    private Long id;
    private String ownerName;
    private LocalDate expirationDate;
    private CardStatus cardStatus;
    private BigDecimal balance;
    private String hiddenNumber;
}

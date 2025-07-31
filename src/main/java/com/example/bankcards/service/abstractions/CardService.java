package com.example.bankcards.service.abstractions;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.CardStatus;
import org.springframework.data.domain.Page;

public interface CardService {
     CardResponse createCard(CardRequest dto);
     void transfer(TransferRequest transferRequest, String username);
     CardResponse blockCard(Long cardId, String username);
     CardResponse toResponse(Card card);
     Page<CardResponse> list(String username, int page, int size);
     Page<CardResponse> listAllCards(int page, int size);
     CardResponse updateCardStatus(Long cardId, CardStatus newStatus);

}

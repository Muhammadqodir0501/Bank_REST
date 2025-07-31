package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.abstractions.CardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Override
    public CardResponse createCard(CardRequest dto) {
        User newCardUser =userRepository.findById(dto.getOwnerId())
                .orElseThrow(()->new RuntimeException("User not found"));
        Card card = Card.builder()
                .number(dto.getNumber())
                .ownerName(dto.getOwnerName())
                .balance(dto.getBalance())
                .status(CardStatus.ACTIVE)
                .expirationDate(dto.getExpirationDate())
                .owner(newCardUser)
                .build();
        Card savedCard = cardRepository.save(card);
        return toResponse(savedCard);
    }
    @Transactional
    @Override
    public void transfer(TransferRequest transferRequest, String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("User not found"));
        Long ownerId = owner.getId();


        Card from = cardRepository.findByIdAndOwnerId(transferRequest.getFromCardId(), ownerId)
                .orElseThrow(() -> new RuntimeException("Sender not found "));

        Card to = cardRepository.findByIdAndOwnerId(transferRequest.getToCardId(), ownerId)
                .orElseThrow(() -> new RuntimeException("Receiver not found "));


        if (from.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        if(from.getId().equals(to.getId())) {
            throw new RuntimeException("Cannot transfer money to the same card");
        }

        from.setBalance(from.getBalance().subtract(transferRequest.getAmount()));
        to.setBalance(to.getBalance().add(transferRequest.getAmount()));

        cardRepository.save(from);
        cardRepository.save(to);
    }

    @Override
    public CardResponse blockCard(Long cardId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Card card = cardRepository.findByIdAndOwnerId(cardId, user.getId())
                .orElseThrow(() -> new RuntimeException("Card not found"));
        card.setStatus(CardStatus.BLOCKED);
        Card savedCard = cardRepository.save(card);
        return toResponse(savedCard);
    }

    @Override
    public CardResponse toResponse(Card card){
        CardResponse response = new CardResponse();
        response.setId(card.getId());
        response.setOwnerName(card.getOwnerName());
        response.setBalance(card.getBalance());
        response.setExpirationDate(card.getExpirationDate());
        response.setCardStatus(card.getStatus());
        response.setHiddenNumber("**** **** **** " + card.getNumber().substring(card.getNumber().length() - 4));
        return response;
    }

    public Page<CardResponse> list(String username, int page, int size) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return cardRepository.findByOwner(user, PageRequest.of(page, size))
                .map(this::toResponse);
    }

    @Override
    public Page<CardResponse> listAllCards(int page, int size) {
        Page<Card> cardsPage = cardRepository.findAll(PageRequest.of(page, size));
        return cardsPage.map(this::toResponse);
    }

    @Override
    public CardResponse updateCardStatus(Long cardId, CardStatus newStatus) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + cardId));

        card.setStatus(newStatus);
        Card savedCard = cardRepository.save(card);

        return toResponse(savedCard);
    }
}

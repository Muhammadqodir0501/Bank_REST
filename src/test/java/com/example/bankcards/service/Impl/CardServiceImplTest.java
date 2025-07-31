package com.example.bankcards.service.Impl;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.CardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardServiceImpl cardService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CardRepository cardRepository;

    private User testUser;
    private Card testCard;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .fullName("Test User")
                .build();

        testCard = Card.builder()
                .id(10L)
                .number("1111222233334444")
                .ownerName("Test User")
                .balance(new BigDecimal("1000.00"))
                .status(CardStatus.ACTIVE)
                .owner(testUser)
                .build();

    }


    @Test
    void createCard_shouldReturnNewCard() {

        CardRequest dto = new CardRequest();
        dto.setOwnerName("Test User");
        dto.setBalance(BigDecimal.ZERO);
        dto.setOwnerId(1L);
        dto.setNumber("1234123412341234");
        dto.setExpirationDate(LocalDate.now().plusYears(1));

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CardResponse response = cardService.createCard(dto);

        assertNotNull(response);
        assertEquals("Test User", response.getOwnerName());
        assertTrue(response.getHiddenNumber().endsWith("1234"));
    }

    @Test
    void blockCard_shouldChangeStatusToBlocked() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(cardRepository.findByIdAndOwnerId(10L, 1L)).thenReturn(Optional.of(testCard));

        cardService.blockCard(10L, "testuser");

        assertEquals(CardStatus.BLOCKED, testCard.getStatus());

        verify(cardRepository, times(1)).save(testCard);
    }

    @Test
    void list_shouldReturnCardPageForUser() {
        Page<Card> cardPage = new PageImpl<>(Collections.singletonList(testCard));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(cardRepository.findByOwner(eq(testUser), any(PageRequest.class))).thenReturn(cardPage);

        Page<CardResponse> resultPage = cardService.list("testuser", 0, 5);

        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalElements());

        assertEquals("Test User", resultPage.getContent().get(0).getOwnerName());
    }


    @Test
    void transfer_shouldSucceed_whenBalanceIsEnough(){

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setToCardId(1L);
        transferRequest.setFromCardId(2L);
        transferRequest.setAmount(new BigDecimal("100.00"));

        User owner = User.builder().id(10L).username("testuser").build();

        Card fromCard = Card.builder().id(1L).owner(owner).balance(new BigDecimal("500.00")).build();
        Card toCard = Card.builder().id(2L).owner(owner).balance(new BigDecimal("200.00")).build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(owner));
        when(cardRepository.findByIdAndOwnerId(1L,10L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByIdAndOwnerId(2L,10L)).thenReturn(Optional.of(toCard));

        cardService.transfer(transferRequest, "testuser");

        assertEquals(new BigDecimal("400.00"), fromCard.getBalance(), "Sender's balance is incorrect");
        assertEquals(new BigDecimal("300.00"), fromCard.getBalance(), "Receiver's balance is incorrect");

        verify(cardRepository, times(1)).save(fromCard);
        verify(cardRepository, times(1)).save(toCard);

    }

    @Test
    void transfer_shouldFail_whenBalanceIsNotEnough(){

        TransferRequest transferDTO = new TransferRequest();
        transferDTO.setFromCardId(1L);
        transferDTO.setToCardId(2L);
        transferDTO.setAmount(new BigDecimal("1000.00")); //more money

        User owner = User.builder().id(10L).username("testuser").build();

        Card fromCard = Card.builder().id(1L).owner(owner).balance(new BigDecimal("500.00")).build();
        Card toCard = Card.builder().id(2L).owner(owner).balance(new BigDecimal("200.00")).build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(owner));
        when(cardRepository.findByIdAndOwnerId(1L, 10L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByIdAndOwnerId(2L, 10L)).thenReturn(Optional.of(toCard));


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cardService.transfer(transferDTO, "testuser");
        });

        assertEquals("Not enough balance", exception.getMessage());

        verify(cardRepository, never()).save(any(Card.class));

    }

}

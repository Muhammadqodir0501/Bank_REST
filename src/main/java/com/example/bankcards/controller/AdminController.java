package com.example.bankcards.controller;

import com.example.bankcards.dto.request.UpdateCardStatusRequest;
import com.example.bankcards.dto.request.UpdateUserStatusRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.abstractions.CardService;
import com.example.bankcards.service.abstractions.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final CardService cardService;
    private final UserService userService;

    @GetMapping("/cards")
    public ResponseEntity<Page<CardResponse>> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(cardService.listAllCards(page, size));
    }

    @PutMapping("/cards/{id}/status")
    public ResponseEntity<CardResponse> updateCardStatus(
            @PathVariable("id") Long cardId,
            @RequestBody UpdateCardStatusRequest request) {

        CardResponse updatedCard = cardService.updateCardStatus(cardId, request.getStatus());
        return ResponseEntity.ok(updatedCard);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.findAllUsers(page, size));
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<UserResponse> updateUserStatus(
            @PathVariable("id") Long userId,
            @RequestBody UpdateUserStatusRequest request) {
        return ResponseEntity.ok(userService.changeUserStatus(userId, request.getStatus()));
    }
}

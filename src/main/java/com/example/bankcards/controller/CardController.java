package com.example.bankcards.controller;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.abstractions.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping("/create")
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CardRequest dto) {
        return  ResponseEntity.ok(cardService.createCard(dto));
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<CardResponse> blockCard(
            @PathVariable Long id,@AuthenticationPrincipal UserDetails userDetails) {
        return  ResponseEntity.ok(cardService.blockCard(id, userDetails.getUsername()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Valid @RequestBody TransferRequest transferRequest,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        cardService.transfer(transferRequest, userDetails.getUsername());
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/get")
    public ResponseEntity<Page<CardResponse>> getMyCards(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(cardService.list(userDetails.getUsername(), page, size));
    }
}

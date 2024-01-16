package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardService cardService;

    @GetMapping("/clients/current/cards")
    public Set<CardDTO> getCards(Authentication authentication) {
        return cardService.getCardsDTOFromClient(authentication);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<String> createCard(@RequestParam CardType type, @RequestParam CardColor color,
                                             Authentication authentication) {
        return cardService.createCard(type, color, authentication);
    }

    @PatchMapping("/clients/current/cards/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable Long id, Authentication authentication) {
        return cardService.deleteCard(id, authentication);
    }

}

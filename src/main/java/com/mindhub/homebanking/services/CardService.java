package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

public interface CardService {

    List<Card> getAllCards();

    List<CardDTO> getAllCardDTO();

    Set<Card> getCardsFromClient(Authentication authentication);

    Set<CardDTO> getCardsDTOFromClient(Authentication authentication);

    ResponseEntity<String> createCard(@RequestParam CardType type, @RequestParam CardColor color,
                                      Authentication authentication);
    Card generateCard(CardType type, CardColor color, Client client);

    void saveToRepository(Card card);
}
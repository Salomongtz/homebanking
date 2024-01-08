package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

public interface CardService {

    public String generateCardNumber();

    public String generateCvv();

    public List<Card> getAllCards();

    public List<CardDTO> getAllCardDTO();

    public Set<Card> getCardsFromClient(Authentication authentication);

    public Set<CardDTO> getCardsDTOFromClient(Authentication authentication);

    public ResponseEntity<String> createCard(@RequestParam CardType type, @RequestParam CardColor color,
                                             Authentication authentication);

    public void saveToRepository(Card card);
}

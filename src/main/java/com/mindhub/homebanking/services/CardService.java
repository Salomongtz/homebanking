package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.records.CardPaymentRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

public interface CardService {

    List<Card> getAllCards();

    List<CardDTO> getAllCardDTO();

    Set<Card> getActiveCardsFromClient(Authentication authentication);

    Set<CardDTO> getCardsDTOFromClient(Authentication authentication);

    ResponseEntity<String> createCard(@RequestParam CardType type, @RequestParam CardColor color,
                                      Authentication authentication);
    ResponseEntity<String> deleteCard(@RequestParam Long id, Authentication authentication);


    Card getCard(CardType type, CardColor color, Client client);

    Card getCardByIdAndClientEmail(Long id, String email);
    CardDTO getCardDTOByIdAndClientEmail(Long id, String email);

    void saveToRepository(Card card);

    ResponseEntity<String> payWithCard(CardPaymentRecord cardPaymentRecord);
}

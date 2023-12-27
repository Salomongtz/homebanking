package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;

    @GetMapping("/cards")
    public List<CardDTO> getCards(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName()).getCards().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<String> createCard(@RequestParam CardType type, @RequestParam CardColor color,
                                             Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (type == null) {
            return ResponseEntity.status(400).body("Missing type data");
        } else if (color == null) {
            return ResponseEntity.status(400).body("Missing color data");
        }
        if (!type.equals(CardType.DEBIT) && !type.equals(CardType.CREDIT)) {
            return ResponseEntity.status(400).body("Invalid card type");
        }
        if (!color.equals(CardColor.GOLD) && !color.equals(CardColor.SILVER) && !color.equals(CardColor.TITANIUM)) {
            return ResponseEntity.status(400).body("Invalid card color");
        }
        if (client.getCards().stream().anyMatch(card -> card.getType().equals(type) && card.getColor().equals(color))) {
            return ResponseEntity.status(403).body("Maximum number of " + color + " " + type + " cards reached");
        }

        String number = generateCardNumber();
        String cvv = generateCvv();
        String cardHolder = client.getFirstName() + " " + client.getLastName();
        LocalDate fromDate = LocalDate.now();
        LocalDate thruDate = fromDate.plusYears(5);
        Card card = new Card(number, cvv, cardHolder, type, color, fromDate, thruDate);
        client.addCard(card);
        cardRepository.save(card);
        return ResponseEntity.status(201).body("The card has been created: " + card);
    }

    public String generateCardNumber() {
        String number;
        do {
            number = "4" + String.format("%03d", new Random().nextInt(0, 1000));
            number += " " + String.format("%04d", new Random().nextInt(0, 10000));
            number += " " + String.format("%04d", new Random().nextInt(0, 10000));
            number += " " + String.format("%04d", new Random().nextInt(0, 10000));
        } while (cardRepository.existsByNumber(number));
        return number;
    }

    public String generateCvv() {
        return String.format("%03d", new Random().nextInt(0, 1000));
    }
}

package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    private static ResponseEntity<String> runVerifications(CardType type, CardColor color, Client client) {
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
        if (client.getCards().stream().anyMatch(card -> card.getType().equals(type) && card.getColor().equals(color) && card.isActive())) {
            return ResponseEntity.status(403).body("Maximum number of " + color + " " + type + " cards reached");
        }

        return null;
    }

    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public List<CardDTO> getAllCardDTO() {
        return getAllCards().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @Override
    public Set<Card> getActiveCardsFromClient(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName()).getCards().stream().filter(Card::isActive).collect(Collectors.toSet());
    }

    @Override
    public Set<CardDTO> getCardsDTOFromClient(Authentication authentication) {
        return getActiveCardsFromClient(authentication).stream().map(CardDTO::new).collect(Collectors.toSet());
    }

    /**
     * Create a new card of the specified type and color for the given client.
     *
     * @param type           the type of the card
     * @param color          the color of the card
     * @param authentication the authentication object for the client
     * @return the response entity containing the status code and the message
     */
    @Override
    public ResponseEntity<String> createCard(CardType type, CardColor color, Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        ResponseEntity<String> Missing_type_data = runVerifications(type, color, client);
        if (Missing_type_data != null) return Missing_type_data;
        Card card = getCard(type, color, client);
        client.addCard(card);
        saveToRepository(card);
        return ResponseEntity.status(201).body("The card has been created: " + card);
    }

    /**
     * Soft deletes a card by its ID by setting its active status to false instead of hard deleting it from the
     * database.
     *
     * @param id             the ID of the card to be deleted
     * @param authentication the authentication object representing the current user
     * @return a ResponseEntity object containing a string indicating the result of the deletion operation
     */
    @Override
    public ResponseEntity<String> deleteCard(Long id, Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        Card card = getCardByIdAndClientEmail(id, authentication.getName());
        if (!client.getCards().contains(card)) {
            return ResponseEntity.status(403).body("The card does not belong to the client");
        }
        if (card == null) {
            return ResponseEntity.status(404).body("Card not found");
        }
        if (!card.isActive()) {
            return ResponseEntity.status(403).body("The card was already deleted");
        }
        if (client.getCards().isEmpty()) {
            return ResponseEntity.status(403).body("No cards can be deleted");
        }
        card.setActive(false);
        saveToRepository(card);
        return ResponseEntity.status(200).body("The card has been deleted");
    }

    /**
     * Generates a new Card object based on the given parameters.
     *
     * @param type   the type of the card (e.g. Visa, Mastercard)
     * @param color  the color of the card (e.g. Blue, Gold)
     * @param client the client object associated with the card
     * @return the newly generated Card object
     */
    @Override
    public Card getCard(CardType type, CardColor color, Client client) {
        String number;
        do {
            number = CardUtils.getCardNumber();
        } while (cardRepository.existsByNumber(number));
        String cvv = CardUtils.getCardCVV();
        String cardHolder = client.getFirstName() + " " + client.getLastName();
        LocalDate fromDate = LocalDate.now();
        LocalDate thruDate = fromDate.plusYears(5);
        return new Card(number, cvv, cardHolder, type, color, fromDate, thruDate);
    }

    @Override
    public Card getCardByIdAndClientEmail(Long id, String email) {
        return cardRepository.findByIdAndClientEmail(id, email);
    }

    @Override
    public CardDTO getCardDTOByIdAndClientEmail(Long id, String email) {
        return new CardDTO(getCardByIdAndClientEmail(id, email));
    }


    @Override
    public void saveToRepository(Card card) {
        cardRepository.save(card);
    }
}

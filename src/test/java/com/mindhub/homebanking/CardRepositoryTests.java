package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardRepositoryTests {
    @Autowired
    private CardRepository cardRepository;

    @Test
    public void existCards() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, is(not(empty())));
    }

    @Test
    public void noNullCardNumbers() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("number", is(notNullValue()))));
    }

    @Test
    public void noNullCVVs() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("cvv", is(notNullValue()))));
    }

    @Test
    public void noNullCardHolders() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("cardHolder", is(notNullValue()))));
    }

    @Test
    public void createCard() {
        // Create a new card
        String number;
        do {
            number = CardUtils.getCardNumber();
        } while (cardRepository.existsByNumber(number));
        String cvv = CardUtils.getCardCVV();
        String cardHolder = "Melba Morel";
        LocalDate fromDate = LocalDate.now();
        LocalDate thruDate = fromDate.plusYears(5);
        Card card= new Card(number, cvv, cardHolder, CardType.DEBIT, CardColor.GOLD, fromDate, thruDate);

        // Save the card to the repository
        Card savedCard = cardRepository.save(card);

        // Retrieve the card by its ID
        Card retrievedCard = cardRepository.findById(savedCard.getId()).orElse(null);

        // Assert that the retrieved card is not null
        assertThat(retrievedCard, is(notNullValue()));

        // Assert that the retrieved card has the same number as the created card
        assertThat(card.getNumber(), is(retrievedCard.getNumber()));

        // Assert that the retrieved card has the same cvv as the created card
        assertThat(card.getCvv(), is(retrievedCard.getCvv()));
    }
}

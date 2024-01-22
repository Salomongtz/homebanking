package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTests {
    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void cardNumberIsCreatedWithSixteenDigits(){
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber.replaceAll(" ", "").length(),is(16));
    }

    @Test
    public void cardCVVIsCreated(){
        String cardCVV = CardUtils.getCardCVV();
        assertThat(cardCVV,is(not(emptyOrNullString())));
    }

    @Test
    public void cardCVVIsCreatedWithThreeDigits(){
        String cardCVV = CardUtils.getCardCVV();
        assertThat(cardCVV.length(),equalTo(3));
    }

}

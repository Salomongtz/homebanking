package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.AccountUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
@SpringBootTest
public class AccountUtilsTests {

    @Test
    public void accountNumberIsCreated() {
        String accountNumber = AccountUtils.getAccountNumber();
        assertThat(accountNumber, is(not(emptyOrNullString())));
    }

    @Test
    public void accountNumberIsCreatedWithSixDigits() {
        String accountNumber = AccountUtils.getAccountNumber();
        assertThat(accountNumber.split("-")[1].length(), is(6));
    }

    @Test
    public void accountNumberStartsWithVIN() {
        String accountNumber = AccountUtils.getAccountNumber();
        assertThat(accountNumber.split("-")[0], is("VIN"));
    }

}

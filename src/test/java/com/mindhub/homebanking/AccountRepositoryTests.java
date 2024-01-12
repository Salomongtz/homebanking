package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTests {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void existAccounts(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts,is(not(empty())));
    }

    @Test
    public void noNullAccountNumbers(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("number", is(notNullValue()))));
    }

    @Test
    public void accountNumberLengthIs6(){
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            assertThat(account.getNumber().split("-")[1].length(), is(6));
        }
    }

    @Test
    public void createAccount() {
        // Create a new account
        Account account = new Account();
        account.setNumber("***REMOVED***");
        account.setBalance(100.0);

        // Save the account to the repository
        Account savedAccount = accountRepository.save(account);

        // Retrieve the account by its ID
        Account retrievedAccount = accountRepository.findById(savedAccount.getId()).orElse(null);

        // Assert that the retrieved account is not null
        assertNotNull(retrievedAccount, "The retrieved account should not be null");

        // Assert that the retrieved account has the same number as the created account
        assertEquals(account.getNumber(), retrievedAccount.getNumber());

        // Assert that the retrieved account has the same balance as the created account
        assertEquals(account.getBalance(), retrievedAccount.getBalance());
    }

}

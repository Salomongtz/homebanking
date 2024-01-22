package com.mindhub.homebanking;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientLoanRepositoryTests {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Test
    public void hasClient() {
        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
        assertThat("Every loan should have a client", clientLoans,
                everyItem(hasProperty("client", notNullValue())));
    }

    @Test
    public void hasLoan() {
        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
        assertThat("Every client should have a loan", clientLoans,
                everyItem(hasProperty("loan", notNullValue())));
    }

}

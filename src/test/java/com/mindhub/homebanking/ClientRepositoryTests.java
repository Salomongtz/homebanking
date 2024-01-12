package com.mindhub.homebanking;

import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.models.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTests {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void existClients() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }

    @Test
    public void emailIsUnique() {
        List<Client> clients = clientRepository.findAll();
        List<Client> distinctClients = clients.stream().distinct().toList();
        assertThat(clients.size(), equalTo(distinctClients.size()));
    }

    @Test
    public void noClientsWithoutAccounts() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("accounts", hasSize(0))));
    }
}

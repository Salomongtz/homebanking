package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<ClientDTO> getAllClientDTO() {
        return getAllClients().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public ClientDTO getClientDTOById(Long id) {
        return getClientById(id) != null ? new ClientDTO(getClientById(id)) : null;
    }

    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public ClientDTO getClientDTOByEmail(String email) {
        return getClientByEmail(email) != null ? new ClientDTO(getClientByEmail(email)) : null;
    }

    @Override
    public ClientDTO getAuthenticatedClientDTO(Authentication authentication) {
        return clientRepository.existsByEmail(authentication.getName()) ?
                new ClientDTO(clientRepository.findByEmail(authentication.getName())) : null;
    }

    @Override
    public ResponseEntity<Object> register(String firstName, String lastName, String email, String password) {
        ResponseEntity<Object> BAD_REQUEST = runVerifications(firstName, lastName, email, password);
        if (BAD_REQUEST != null) return BAD_REQUEST;

        Client client = generateClient(firstName, lastName, email, password);
        Account account = accountService.generateAccount(AccountType.CHECKING);
        client.addAccount(account);
        saveToRepository(client);
        accountRepository.save(account);

        return new ResponseEntity<>(client + "\nCreated successfully!", HttpStatus.CREATED);
    }

    private Client generateClient(String firstName, String lastName, String email, String password) {
        return new Client(firstName, lastName, email, passwordEncoder.encode(password));
    }

    private ResponseEntity<Object> runVerifications(String firstName, String lastName, String email, String password) {
        if (firstName.isBlank()) {
            return new ResponseEntity<>("Missing NAME data", HttpStatus.BAD_REQUEST);
        } else if (lastName.isBlank()) {
            return new ResponseEntity<>("Missing LAST NAME data", HttpStatus.BAD_REQUEST);
        } else if (email.isBlank()) {
            return new ResponseEntity<>("Missing EMAIL data", HttpStatus.BAD_REQUEST);
        } else if (password.isBlank()) {
            return new ResponseEntity<>("Missing PASSWORD data", HttpStatus.BAD_REQUEST);
        }
        if (getClientByEmail(email) != null) {
            return new ResponseEntity<>(email + " already in use", HttpStatus.FORBIDDEN);
        }
        return null;
    }

    @Override
    public void saveToRepository(Client client) {
        clientRepository.save(client);
    }
}

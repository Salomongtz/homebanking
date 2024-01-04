package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;

import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO findClient(@PathVariable Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    /**
     * Creates an account for the current client and returns a response entity.
     *
     * @return A response entity containing the account details and a success message.
     */
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<String> createAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        System.out.println(client.toString());

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Maximum number of accounts reached.", HttpStatus.FORBIDDEN);
        }

        String number = generateAccountNumber();
        Account account = new Account(number, LocalDate.now(), 0);
        client.addAccount(account);
        accountRepository.save(account);
        return new ResponseEntity<>(account + "\nCreated successfully!", HttpStatus.CREATED);
    }

    /**
     * Generates a unique account number by concatenating the prefix "VIN-" with a randomly generated 6-digit number.
     * The generated account number is checked against the account repository to ensure uniqueness.
     *
     * @return  the generated unique account number
     */
    private String generateAccountNumber() {
        String number;
        do {
            number = "VIN-" + String.format("%06d", new Random().nextInt(0, 1000000));
        } while (accountRepository.existsByNumber(number));
        return number;
    }
}

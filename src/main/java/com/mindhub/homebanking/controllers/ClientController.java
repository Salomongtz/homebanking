package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CardService cardService;

    @GetMapping()
    public List<ClientDTO> getClients() {
        return clientService.getAllClientDTO();
    }

    @GetMapping("{id}")
    public ClientDTO getClientDTOById(@PathVariable Long id) {
        return clientService.getClientDTOById(id);
    }

    @GetMapping("/current")
    public ClientDTO getAuthenticatedClientDTO(Authentication authentication) {
        return clientService.getAuthenticatedClientDTO(authentication);
    }

    @GetMapping("/current/accounts")
    public Set<AccountDTO> getAccounts(Authentication authentication) {
        return accountService.getAccountsDTOFromClient(authentication);
    }

    @PostMapping
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

       return clientService.register(firstName, lastName, email, password);
    }

}

package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;

import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAllAccountDTO();
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO findById(@PathVariable Long id) {
        return accountService.getAccountDTOById(id);
    }

    /**
     * Creates an account for the current client and returns a response entity.
     *
     * @return A response entity containing the account details and a success message.
     */
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<String> createAccount(Authentication authentication) {
        return accountService.createAccount(authentication);
    }


}

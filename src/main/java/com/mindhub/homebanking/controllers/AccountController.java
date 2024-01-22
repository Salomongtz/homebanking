package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.ClientLoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccounts(Authentication authentication) {
        return accountService.getAccountsDTOFromClient(authentication);
    }

    @GetMapping("/clients/current/accounts/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id, Authentication authentication) {
        Account account = accountService.getAccountByIdAndClientEmail(id, authentication.getName());
        if (accountService.getAccountById(id) == null) {
            return ResponseEntity.status(404).body("Account not found.");
        }
        if (!account.isActive()) {
            return ResponseEntity.status(403).body("Account deleted.");
        }
        return ResponseEntity.status(200).body(new AccountDTO(account));
    }

    @PatchMapping("/clients/current/accounts/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id, Authentication authentication) {
        return accountService.deleteAccount(id, authentication);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<String> createAccount(@RequestParam AccountType type, Authentication authentication) {
        return accountService.createAccount(authentication, type);
    }

    @PatchMapping("/clients/current/accounts/payLoan")
    @Transactional
    public ResponseEntity<String> payLoan(@RequestParam Long id, @RequestParam String accountNumber, Authentication authentication) {
        return accountService.payLoan(id, accountNumber, authentication);
    }

}

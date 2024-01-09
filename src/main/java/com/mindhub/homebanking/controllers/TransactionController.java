package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping
    public ResponseEntity<String> createTransaction(@RequestParam double transactionAmount,
                                                    @RequestParam String transactionDescription,
                                                    @RequestParam String originAccountNumber,
                                                    @RequestParam String destinationAccountNumber,
                                                    Authentication authentication) {

        return transactionService.createTransaction(transactionAmount, transactionDescription, originAccountNumber, destinationAccountNumber, authentication);
    }

}

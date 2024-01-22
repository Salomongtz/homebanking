package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TransactionService {

    List<Transaction> getAllTransactions();

    List<TransactionDTO> getAllTransactionDTO();

    void createTransaction(String description, Double amount, Account account);

    ResponseEntity<String> createTransaction(double transactionAmount, String transactionDescription,
                                             String originAccountNumber, String destinationAccountNumber,
                                             Authentication authentication);
}

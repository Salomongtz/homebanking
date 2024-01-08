package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dto.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getAllTransactions() {
        return null;
    }

    @Override
    public List<TransactionDTO> getAllTransactionDTO() {
        return null;
    }

    @Override
    public void createTransaction(String description, Double amount, Account account) {
        TransactionType transactionType = amount > 0 ? TransactionType.CREDIT : TransactionType.DEBIT;
        Transaction transaction = new Transaction(transactionType, amount,
                LocalDate.now(), description);
        account.addTransaction(transaction);
        transactionRepository.save(transaction);
        account.setBalance(account.getBalance() + amount);
    }
}


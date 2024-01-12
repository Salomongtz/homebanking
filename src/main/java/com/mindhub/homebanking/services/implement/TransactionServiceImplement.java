package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dto.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    private static ResponseEntity<String> runVerifications(double transactionAmount, String transactionDescription,
                                                           String originAccountNumber,
                                                           String destinationAccountNumber, Account originAccount,
                                                           Client client, Account destinationAccount) {
        //Verificar que los parámetros no estén vacíos
        if (originAccountNumber.isBlank()) {
            return new ResponseEntity<>("The origin account number cannot be empty", HttpStatus.FORBIDDEN);
        } else if (destinationAccountNumber.isBlank()) {
            return new ResponseEntity<>("The destination account number cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (transactionAmount <= 0) {
            return new ResponseEntity<>("The amount must be greater than 0", HttpStatus.FORBIDDEN);
        }
        if (transactionDescription.isBlank()) {
            return new ResponseEntity<>("The description cannot be empty", HttpStatus.FORBIDDEN);
        }

        //Verificar que los números de cuenta no sean iguales
        if (originAccountNumber.equals(destinationAccountNumber)) {
            return new ResponseEntity<>("Origin and destination accounts cannot be the same", HttpStatus.FORBIDDEN);
        }

        //Verificar que exista la cuenta de origen
        if (originAccount == null) {
            return new ResponseEntity<>("Origin account not found", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de origen pertenezca al cliente autenticado
        if (!client.getAccounts().contains(originAccount)) {
            return new ResponseEntity<>("Origin account does not belong to the current client", HttpStatus.FORBIDDEN);
        }

        //Verificar que exista la cuenta de destino
        if (destinationAccount == null) {
            return new ResponseEntity<>("Destination account not found", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de origen tenga el monto disponible.
        if (originAccount.getBalance() < transactionAmount) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }
        return null;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<TransactionDTO> getAllTransactionDTO() {
        return getAllTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
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

    @Override
    public ResponseEntity<String> createTransaction(double transactionAmount, String transactionDescription,
                                                    String originAccountNumber, String destinationAccountNumber,
                                                    Authentication authentication) {
        Account originAccount, destinationAccount;
        originAccount = accountRepository.findByNumber(originAccountNumber);
        destinationAccount = accountRepository.findByNumber(destinationAccountNumber);
        Client client = clientRepository.findByEmail(authentication.getName());

        ResponseEntity<String> FORBIDDEN = runVerifications(transactionAmount, transactionDescription,
                originAccountNumber, destinationAccountNumber, originAccount, client, destinationAccount);
        if (FORBIDDEN != null) return FORBIDDEN;

        //Se deben crear dos transacciones, una con el tipo de transacción “DEBIT” asociada a la cuenta de origen y
        // la otra con el tipo de transacción “CREDIT” asociada a la cuenta de destino.
        Transaction originTransaction = new Transaction(TransactionType.DEBIT, -transactionAmount, LocalDate.now(),
                transactionDescription);
        Transaction destinationTransaction = new Transaction(TransactionType.CREDIT, transactionAmount,
                LocalDate.now(), transactionDescription);

        //A la cuenta de origen se le restará el monto indicado en la petición y a la cuenta de destino se le sumará
        // el mismo monto.
        originAccount.addTransaction(originTransaction);
        destinationAccount.addTransaction(destinationTransaction);
        originAccount.setBalance(originAccount.getBalance() - transactionAmount);
        destinationAccount.setBalance(destinationAccount.getBalance() + transactionAmount);
        transactionRepository.save(originTransaction);
        transactionRepository.save(destinationTransaction);

        return new ResponseEntity<>("Transaction completed!", HttpStatus.CREATED);
    }
}


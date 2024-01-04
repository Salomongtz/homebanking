package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping
    public ResponseEntity<String> createTransaction(@RequestParam double transactionAmount,
                                                    @RequestParam String transactionDescription,
                                                    @RequestParam String originAccountNumber,
                                                    @RequestParam String destinationAccountNumber,
                                                    Authentication authentication) {

        Account originAccount, destinationAccount;

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


        originAccount = accountRepository.findByNumber(originAccountNumber);
        destinationAccount = accountRepository.findByNumber(destinationAccountNumber);

        //Verificar que exista la cuenta de origen
        if (originAccount == null) {
            return new ResponseEntity<>("Origin account not found", HttpStatus.FORBIDDEN);
        }

        Client client = clientRepository.findByEmail(authentication.getName());

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

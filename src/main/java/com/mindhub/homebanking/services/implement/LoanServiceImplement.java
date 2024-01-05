package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.records.LoanApplicationRecord;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImplement implements LoanService {

    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public List<LoanDTO> getAllLoanDTO() {
        return getAllLoans().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public LoanDTO getLoanDTOById(Long id) {
        return new LoanDTO(getLoanById(id));
    }

    @Override
    public ResponseEntity<String> createLoan(LoanApplicationRecord loanApplicationRecord,
                                             Authentication authentication) {
        Loan loan = getLoanById(loanApplicationRecord.id());
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(loanApplicationRecord.accountNumber());
        Double amount = loanApplicationRecord.amount();

        if (amount <= 0) {
            return new ResponseEntity<>("The amount must be greater than 0", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationRecord.payments() <= 0) {
            return new ResponseEntity<>("The payments must be greater than 0", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationRecord.accountNumber().isBlank()) {
            return new ResponseEntity<>("The account number cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (!loanRepository.existsById(loanApplicationRecord.id())) {
            return new ResponseEntity<>("The loan does not exist", HttpStatus.FORBIDDEN);
        }
        assert loan != null;
        if (!loan.getPayments().contains(loanApplicationRecord.payments())) {
            return new ResponseEntity<>("Invalid payments amount", HttpStatus.FORBIDDEN);
        }
        if (amount > loan.getMaxAmount()) {
            return new ResponseEntity<>("Loan exceeds the maximum amount. The maximum amount is " + loan.getMaxAmount(), HttpStatus.FORBIDDEN);
        }
        if (!accountRepository.existsByNumber(loanApplicationRecord.accountNumber())) {
            return new ResponseEntity<>("The account does not exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(accountRepository.findByNumber(loanApplicationRecord.accountNumber()))) {
            return new ResponseEntity<>("The account does not belong to the client", HttpStatus.FORBIDDEN);
        }

        ClientLoan loanApplication = new ClientLoan(amount * 1.2, loanApplicationRecord.payments());
        loan.addClientLoan(loanApplication);
        client.addClientLoans(loanApplication);
        clientLoanRepository.save(loanApplication);

        createTransaction(loan.getName() + " loan approved", amount, account);
        return new ResponseEntity<String>("Loan created successfully!", HttpStatus.CREATED);
    }

    public void createTransaction(String description, Double amount, Account account) {
        TransactionType transactionType = amount > 0 ? TransactionType.CREDIT : TransactionType.DEBIT;
        Transaction transaction = new Transaction(transactionType, amount,
                LocalDate.now(), description);
        account.addTransaction(transaction);
        transactionRepository.save(transaction);
        account.setBalance(account.getBalance() + amount);
    }
}


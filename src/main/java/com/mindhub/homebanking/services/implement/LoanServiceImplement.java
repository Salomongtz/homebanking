package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.records.LoanApplicationRecord;
import com.mindhub.homebanking.records.LoanRecord;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
    TransactionService transactionService;

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
        try {
            Loan loan = getLoanById(loanApplicationRecord.id());
            Client client = clientRepository.findByEmail(authentication.getName());
            Account account = accountRepository.findByNumber(loanApplicationRecord.accountNumber());
            Double amount = loanApplicationRecord.amount();

            ResponseEntity<String> FORBIDDEN = runVerifications(loanApplicationRecord, amount, loan, client);
            if (FORBIDDEN != null) return FORBIDDEN;

            createClientLoan(loanApplicationRecord, amount, loan, client);

            transactionService.createTransaction(loan.getName() + " loan approved", amount, account);
            return new ResponseEntity<>("Loan created successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> newLoanType(LoanRecord loanRecord) {
        Loan loan = loanRepository.findByName(loanRecord.name());
        if (loan != null) {
            return new ResponseEntity<>("Loan type already exists", HttpStatus.FORBIDDEN);
        }
        if (loanRecord.interestRate() < 0) {
            return new ResponseEntity<>("Interest rate cannot be negative", HttpStatus.FORBIDDEN);
        }
        if (loanRecord.maxAmount() < 0) {
            return new ResponseEntity<>("Max amount cannot be negative", HttpStatus.FORBIDDEN);
        }
        if (loanRecord.payments().isEmpty()) {
            return new ResponseEntity<>("Payments cannot be negative", HttpStatus.FORBIDDEN);
        }
        Loan newLoan = new Loan(loanRecord.name(), loanRecord.maxAmount(), loanRecord.payments(),
                loanRecord.interestRate());
        loanRepository.save(newLoan);
        return new ResponseEntity<>("Loan type created successfully!", HttpStatus.CREATED);
    }

    public double calculateInterestByPaymentAmount(double interest, int payments) {
        double aux = interest;
        for (int i = 0; i < payments / 2; i++) {
            aux += 0.025;
        }
        return aux;
    }

    private void createClientLoan(LoanApplicationRecord loanApplicationRecord, Double amount, Loan loan,
                                  Client client) {
        double interest = calculateInterestByPaymentAmount(loan.getInterestRate(), loanApplicationRecord.payments());
        ClientLoan loanApplication = new ClientLoan(amount * interest, loanApplicationRecord.payments());
        loan.addClientLoan(loanApplication);
        client.addClientLoans(loanApplication);
        clientLoanRepository.save(loanApplication);
    }

    private ResponseEntity<String> runVerifications(LoanApplicationRecord loanApplicationRecord, Double amount,
                                                    Loan loan, Client client) {
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
        return null;
    }


}


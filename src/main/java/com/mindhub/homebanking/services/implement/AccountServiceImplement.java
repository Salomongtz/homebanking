package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.ClientLoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.TransactionService;
import com.mindhub.homebanking.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    TransactionService transactionService;

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public List<AccountDTO> getAllAccountDTO() {
        return getAllAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public AccountDTO getAccountDTOById(Long id) {
        return new AccountDTO(getAccountById(id));
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByNumber(accountNumber);
    }

    @Override
    public AccountDTO getAccountDTOByNumber(String accountNumber) {
        return new AccountDTO(getAccountByNumber(accountNumber));
    }

    @Override
    public Account getAccountByIdAndClientEmail(Long id, String email) {
        return accountRepository.findByIdAndClientEmail(id, email);
    }

    @Override
    public AccountDTO getAccountDTOByIdAndClientEmail(Long id, String email) {
        return new AccountDTO(getAccountByIdAndClientEmail(id, email));
    }

    @Override
    public Set<Account> getAccountsFromClient(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName()).getAccounts();
    }

    @Override
    public Set<AccountDTO> getAccountsDTOFromClient(Authentication authentication) {
        return getAccountsFromClient(authentication).stream().filter(Account::isActive)
                .map(AccountDTO::new).collect(Collectors.toSet());
    }

    @Override
    public void updateBalance(Account account, Double amount) {
        account.setBalance(amount);
    }

    @Override
    public ResponseEntity<String> createAccount(Authentication authentication, AccountType type) {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getAccounts().stream().filter(Account::isActive).toList().size() >= 3) {
            return new ResponseEntity<>("Maximum number of accounts reached.", HttpStatus.FORBIDDEN);
        }

        Account account;

        do {
            account = generateAccount(type);
        } while (accountRepository.existsByNumber(account.getNumber()));
        client.addAccount(account);
        saveToRepository(account);
        return new ResponseEntity<>(account + "\nCreated successfully!", HttpStatus.CREATED);
    }

    @Override
    public Account generateAccount(AccountType type) {
        String number = AccountUtils.getAccountNumber();
        return new Account(number, LocalDate.now(), 0, type);
    }

    @Override
    public ResponseEntity<String> deleteAccount(Long id, Authentication authentication) {
        Account account = getAccountByIdAndClientEmail(id, authentication.getName());

        if (account == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
        if (account.getClient().getAccounts().stream().filter(Account::isActive).toList().size() == 1) {
            return new ResponseEntity<>("Cannot delete: Client must have at least one active account",
                    HttpStatus.FORBIDDEN);
        }
        if (!account.isActive()) {
            return new ResponseEntity<>("Account already deleted", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance() > 0) {
            return new ResponseEntity<>("The account has a balance, please withdraw it first", HttpStatus.FORBIDDEN);
        }

        account.setActive(false);
        saveToRepository(account);
        return ResponseEntity.status(HttpStatus.OK).body("Account deleted successfully");
    }

    @Override
    public void saveToRepository(Account account) {
        accountRepository.save(account);
    }

    @Override
    public ResponseEntity<String> payLoan(Long id, String accountNumber, Authentication authentication) {
        ClientLoan clientLoan = clientLoanRepository.findById(id).orElse(null);
        Account account = accountRepository.findByNumber(accountNumber);

        if (clientLoan == null) {
            return new ResponseEntity<>("Loan not found", HttpStatus.NOT_FOUND);
        }
        if (account == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
        if (account.getClient().getEmail().equals(authentication.getName()) && !account.isActive()) {
            return new ResponseEntity<>("Account not active", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance() < clientLoan.getAmount()/clientLoan.getPayments()) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }
        if (clientLoan.getPayments() == 0) {
            return new ResponseEntity<>("Loan already paid", HttpStatus.FORBIDDEN);
        }
        transactionService.createTransaction("Loan payment. "+"Remaining payments: " + (clientLoan.getPayments()-1), -clientLoan.getAmount()/clientLoan.getPayments(), account);
        clientLoan.setPayments(clientLoan.getPayments() - 1);
        clientLoan.setAmount(clientLoan.getAmount() - (clientLoan.getAmount() / clientLoan.getPayments()));
        return ResponseEntity.status(HttpStatus.OK).body("Loan paid successfully" + "Remaining debt: " + clientLoan.getAmount() + "Remaining payments: " + clientLoan.getPayments());
    }
}

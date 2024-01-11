package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;

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
    public Set<Account> getAccountsFromClient(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName()).getAccounts();
    }

    @Override
    public Set<AccountDTO> getAccountsDTOFromClient(Authentication authentication) {
        return getAccountsFromClient(authentication).stream().map(AccountDTO::new).collect(Collectors.toSet());
    }

    @Override
    public void updateBalance(Account account, Double amount) {
        account.setBalance(amount);
    }

    @Override
    public ResponseEntity<String> createAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Maximum number of accounts reached.", HttpStatus.FORBIDDEN);
        }

        Account account = generateAccount();
        client.addAccount(account);
        saveToRepository(account);
        return new ResponseEntity<>(account + "\nCreated successfully!", HttpStatus.CREATED);
    }
    @Override
    public Account generateAccount() {
        String number = generateAccountNumber();
        return new Account(number, LocalDate.now(), 0);
    }

    @Override
    public String generateAccountNumber() {
        String number;
        do {
            number = "VIN-" + String.format("%06d", new Random().nextInt(0, 1000000));
        } while (accountRepository.existsByNumber(number));
        return number;
    }

    @Override
    public void saveToRepository(Account account) {
        accountRepository.save(account);
    }
}

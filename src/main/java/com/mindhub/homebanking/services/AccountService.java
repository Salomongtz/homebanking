package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface AccountService {

    public List<Account> getAllAccounts();

    public List<AccountDTO> getAllAccountDTO();

    public Account getAccountById(Long id);

    public AccountDTO getAccountDTOById(Long id);

    public Account getAccountByNumber(String accountNumber);

    public AccountDTO getAccountDTOByNumber(String accountNumber);

    public Set<Account> getAccountsFromClient(Authentication authentication);

    public Set<AccountDTO> getAccountsDTOFromClient(Authentication authentication);

    public void updateBalance(Account account, Double amount);

    public ResponseEntity<String> createAccount(Authentication authentication);

    public String generateAccountNumber();

    public void saveToRepository(Account account);

}

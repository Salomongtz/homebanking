package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface AccountService {

    List<Account> getAllAccounts();

    List<AccountDTO> getAllAccountDTO();

    Account getAccountById(Long id);

    AccountDTO getAccountDTOById(Long id);

    Account getAccountByNumber(String accountNumber);

    AccountDTO getAccountDTOByNumber(String accountNumber);

    Set<Account> getAccountsFromClient(Authentication authentication);

    Set<AccountDTO> getAccountsDTOFromClient(Authentication authentication);

    void updateBalance(Account account, Double amount);

    ResponseEntity<String> createAccount(Authentication authentication);

    Account generateAccount();

    void saveToRepository(Account account);

}

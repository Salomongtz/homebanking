package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.ClientLoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
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
    Account getAccountByIdAndClientEmail(Long id, String email);
    AccountDTO getAccountDTOByIdAndClientEmail(Long id, String email);

    Set<Account> getAccountsFromClient(Authentication authentication);

    Set<AccountDTO> getAccountsDTOFromClient(Authentication authentication);

    void updateBalance(Account account, Double amount);

    ResponseEntity<String> createAccount(Authentication authentication, AccountType type);
    ResponseEntity<String> deleteAccount(Long id, Authentication authentication);

    Account generateAccount(AccountType type);

    void saveToRepository(Account account);

    ResponseEntity<String> payLoan(Long id, String accountNumber, Authentication authentication);
}

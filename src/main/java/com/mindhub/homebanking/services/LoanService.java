package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.records.LoanApplicationRecord;
import com.mindhub.homebanking.records.LoanRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface LoanService {

    List<Loan> getAllLoans();

    List<LoanDTO> getAllLoanDTO();

    Loan getLoanById(Long id);

    LoanDTO getLoanDTOById(Long id);

    ResponseEntity<String> createLoan(LoanApplicationRecord loanApplicationRecord,
                                      Authentication authentication);

    ResponseEntity<String> newLoanType(LoanRecord loanRecord);
}

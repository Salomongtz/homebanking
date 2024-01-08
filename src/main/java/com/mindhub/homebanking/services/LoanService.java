package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.records.LoanApplicationRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface LoanService {

    public List<Loan> getAllLoans();

    public List<LoanDTO> getAllLoanDTO();

    public Loan getLoanById(Long id);

    public LoanDTO getLoanDTOById(Long id);

    public ResponseEntity<String> createLoan(LoanApplicationRecord loanApplicationRecord,
                                             Authentication authentication);
}

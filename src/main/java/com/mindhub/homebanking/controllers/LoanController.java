package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.records.LoanApplicationRecord;
import com.mindhub.homebanking.records.LoanRecord;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    LoanService loanService;

    @GetMapping
    public List<LoanDTO> getLoans() {
        return loanService.getAllLoanDTO();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> createLoan(@RequestBody LoanApplicationRecord loanApplicationRecord,
                                             Authentication authentication) {
        return loanService.createLoan(loanApplicationRecord, authentication);
    }

    @PostMapping("/new")
    public ResponseEntity<String> newLoanType(@RequestBody LoanRecord loanRecord) {
        return loanService.newLoanType(loanRecord);
    }
}

package com.mindhub.homebanking.records;

public record LoanApplicationRecord(Long id, Double amount, Integer payments, String accountNumber) {
}

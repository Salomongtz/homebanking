package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Loan;

import java.util.HashSet;
import java.util.Set;

public class LoanDTO {
private final Long id;
private final String name;
private final Set<Integer> payments = new HashSet<>();
private final Double maxAmount;

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Integer> getPayments() {
        return payments;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }
}

package com.mindhub.homebanking.records;

import java.util.List;

public record LoanRecord (String name, Double maxAmount, List<Integer> payments, float interestRate) {
}

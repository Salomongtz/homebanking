package com.mindhub.homebanking.records;

public record CardPaymentRecord(String number, String cvv, Double amount, String description) {
}

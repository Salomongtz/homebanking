package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private Set<Integer> payments = new HashSet<>();

    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans;

    private String name;
    private Integer maxAmount;

    public void addClientLoan(ClientLoan clientLoan){
        this.clientLoans.add(clientLoan);
        clientLoan.setLoan(this);
    }

    public Loan(String name, Integer maxAmount, Set<Integer>payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments=payments;
    }

    public Loan() {
    }

    public Long getId() {
        return id;
    }

    public Set<Integer> getPayments() {
        return payments;
    }

    public void setPayments(Set<Integer> payments) {
        this.payments = payments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }
    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", payments=" + payments +
                ", name='" + name + '\'' +
                ", maxAmount=" + maxAmount +
                '}';
    }
}

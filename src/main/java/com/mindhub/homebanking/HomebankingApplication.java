package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
                                      TransactionRepository transactionRepository, LoanRepository loanRepository,
                                      ClientLoanRepository clientLoanRepository) {
        return args -> {
            Client melba = new Client("Melba", "Morel", "***REMOVED***");
            Client salomon = new Client("Salomon", "Gutierrez", "***REMOVED***");
            clientRepository.save(melba);
            clientRepository.save(salomon);

            Account account1 = new Account("VIN001", LocalDate.now(), 5000);
            Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500);
            Account account3 = new Account("VIN003", LocalDate.now(), 10000);

            melba.addAccount(account1);
            melba.addAccount(account2);
            salomon.addAccount(account3);

            accountRepository.save(account1);
            accountRepository.save(account2);
            accountRepository.save(account3);

            Transaction t1 = new Transaction(TransactionType.DEBIT, -200, LocalDate.now(), "Regalo para sobrino.");
            Transaction t2 = new Transaction(TransactionType.CREDIT, 2000, LocalDate.now(), "Boda Antonio.");
            Transaction t3 = new Transaction(TransactionType.CREDIT, 900, LocalDate.now(), "Fiesta Melbita.");
            Transaction t4 = new Transaction(TransactionType.DEBIT, -1300, LocalDate.now(), "Seguro Auto.");
            Transaction t5 = new Transaction(TransactionType.CREDIT, 499.99, LocalDate.now(), "PS5");

            account1.addTransaction(t1);
            account1.addTransaction(t2);
            account2.addTransaction(t3);
            account3.addTransaction(t4);
            account3.addTransaction(t5);

            transactionRepository.save(t1);
            transactionRepository.save(t2);
            transactionRepository.save(t3);
            transactionRepository.save(t4);
            transactionRepository.save(t5);

            Loan loan1 = new Loan("Mortgage", 500000, Set.of(12,24,36,48,60));
            Loan loan2 = new Loan("Personal", 100000, Set.of(6,12,24));
            Loan loan3 = new Loan("Automotive", 300000, Set.of(6,12,24,36));

            loanRepository.saveAll(List.of(loan1,loan2,loan3));

            ClientLoan melbaMortgage=new ClientLoan(400000,60);
            ClientLoan melbaPersonal=new ClientLoan(50000,12);
            ClientLoan salomonPersonal=new ClientLoan(100000,24);
            ClientLoan salomonAutomotive=new ClientLoan(400000,60);

            clientLoanRepository.saveAll(List.of(melbaMortgage,melbaPersonal,salomonPersonal,salomonAutomotive));

            melba.addClientLoans(melbaMortgage);
            melba.addClientLoans(melbaPersonal);
            salomon.addClientLoans(salomonPersonal);
            salomon.addClientLoans(salomonAutomotive);
        };
    }

}

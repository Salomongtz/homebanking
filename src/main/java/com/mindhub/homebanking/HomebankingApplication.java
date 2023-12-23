package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Autowired
    public PasswordEncoder passwordEncoder;
    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
                                      TransactionRepository transactionRepository, LoanRepository loanRepository,
                                      ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
        return args -> {
            Client melba = new Client("Melba", "Morel", "***REMOVED***",passwordEncoder.encode("***REMOVED***"));
            Client salomon = new Client("Salomon", "Gutierrez", "***REMOVED***", passwordEncoder.encode("***REMOVED***"));
            Client admin = new Client("David", "Bent", "david@admin.com", passwordEncoder.encode("***REMOVED***"));
            admin.setRole(RoleType.ADMIN);
            clientRepository.save(melba);
            clientRepository.save(salomon);
            clientRepository.save(admin);

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
            Transaction t3 = new Transaction(TransactionType.CREDIT, 900, LocalDate.now(), "Fiesta Melba.");
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

            Loan mortgage = new Loan("Mortgage", 500000, Set.of(12, 24, 36, 48, 60));
            Loan personal = new Loan("Personal", 100000, Set.of(6, 12, 24));
            Loan automotive = new Loan("Automotive", 300000, Set.of(6, 12, 24, 36));

            loanRepository.saveAll(List.of(mortgage, personal, automotive));

            ClientLoan melbaMortgage = new ClientLoan(400000, 60);
            ClientLoan melbaPersonal = new ClientLoan(50000, 12);
            ClientLoan salomonPersonal = new ClientLoan(100000, 24);
            ClientLoan salomonAutomotive = new ClientLoan(400000, 60);

            melba.addClientLoans(melbaMortgage);
            melba.addClientLoans(melbaPersonal);
            salomon.addClientLoans(salomonPersonal);
            salomon.addClientLoans(salomonAutomotive);

            mortgage.addClientLoan(melbaMortgage);
            personal.addClientLoan(melbaPersonal);
            personal.addClientLoan(salomonPersonal);
            automotive.addClientLoan(salomonAutomotive);

            clientLoanRepository.saveAll(List.of(melbaMortgage, melbaPersonal, salomonPersonal, salomonAutomotive));

            Card debitGoldMelba = new Card("1234 5678 9101 1121","911", "Melba Morel", CardType.DEBIT, CardColor.GOLD, LocalDate.now(),LocalDate.now().plusYears(5));
            Card creditTitaniumMelba = new Card("9929 1239 8980 2093","626", "Melba Morel", CardType.CREDIT, CardColor.TITANIUM, LocalDate.now(),LocalDate.now().plusYears(5));
            Card creditSilverSalomon = new Card("9878 7231 2312 1313","621", "Salomón Gutiérrez" ,CardType.CREDIT, CardColor.TITANIUM, LocalDate.now(),LocalDate.now().plusYears(5));

            melba.addCard(debitGoldMelba);
            melba.addCard(creditTitaniumMelba);
            salomon.addCard(creditSilverSalomon);

            cardRepository.saveAll(List.of(debitGoldMelba,creditTitaniumMelba,creditSilverSalomon));

        };
    }

}

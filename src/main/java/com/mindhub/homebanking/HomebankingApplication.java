package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
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

        };
    }

}

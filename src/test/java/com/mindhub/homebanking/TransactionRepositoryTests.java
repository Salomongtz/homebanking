package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTests {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void existTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));
    }

    @Test
    public void typeIsEitherDebitOrCredit() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("type",
                is(either(is(TransactionType.DEBIT)).or(is(TransactionType.CREDIT))))));
    }

    @Test
    public void descriptionIsNotNull() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("description", notNullValue())));
    }

    @Test
    public void descriptionIsShorterThan255() {
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction : transactions)
            assertThat(transaction.getDescription().length(), is(lessThanOrEqualTo(255)));
    }


}

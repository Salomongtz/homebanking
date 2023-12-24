package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByNumber(String number);

    boolean existsByCvv(String cvv);

    Collection<Card> findByTypeAndClientId(CardType type, Long id);
}

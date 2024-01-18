package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByNumber(String number);

    boolean existsByCvv(String cvv);

    @Query("SELECT c FROM Card c " +
            "WHERE c.id = :id AND " +
            "c.client.id IN ( " +
            "SELECT cl.id FROM Client cl " +
            "WHERE cl.email LIKE :email)")
    Card findByIdAndClientEmail(@Param("id") Long id, @Param("email") String email);

    Card findByNumber(String number);
}

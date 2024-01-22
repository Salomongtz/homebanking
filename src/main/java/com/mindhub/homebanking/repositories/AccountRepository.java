package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByNumber(String number);

    Account findByNumber(String accountNumber);
    @Query("SELECT a FROM Account a "+
            "WHERE a.id = :id AND " +
            "a.client.id IN ( "+
            "SELECT c.id FROM Client c "+
            "WHERE c.email LIKE :email)")
    Account findByIdAndClientEmail(@Param("id") Long id, @Param("email") String email);
}

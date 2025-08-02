// File: src/main/java/com/bank/repository/AccountRepository.java

package com.bank.repository;

import com.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Account entity.
 * By extending JpaRepository, we get a lot of standard CRUD (Create, Read, Update, Delete)
 * methods for free, like save(), findById(), findAll(), deleteById(), etc.
 *
 * Spring will automatically implement this interface at runtime.
 *
 * @param <Account> The domain type the repository manages (Account).
 * @param <Integer> The type of the id of the domain type (the type of Account.accountId).
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    // We don't need to write any code here for basic CRUD!
    // We can add custom query methods later if needed.
}

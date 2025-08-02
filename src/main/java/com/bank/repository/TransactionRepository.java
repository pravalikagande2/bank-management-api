// File: src/main/java/com/bank/repository/TransactionRepository.java

package com.bank.repository;

import com.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Spring Data JPA repository for the Transaction entity.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    /**
     * This is a custom "derived query". Spring Data JPA will automatically
     * create the implementation for this method based on its name.
     * It will generate a query equivalent to:
     * "SELECT * FROM transactions WHERE accountId = ? ORDER BY transactionTime DESC"
     *
     * @param accountId The account ID to search for.
     * @return A list of transactions for the given account.
     */
    List<Transaction> findByAccountIdOrderByTransactionTimeDesc(Integer accountId);

    /**
     * Another derived query for our fraud detection service.
     * It will generate a query equivalent to:
     * "SELECT * FROM transactions WHERE accountId = ? AND transactionTime >= ?"
     *
     * @param accountId The account ID.
     * @param windowStart The start of the time window.
     * @return A list of transactions within the time window.
     */
    List<Transaction> findByAccountIdAndTransactionTimeGreaterThanEqual(Integer accountId, Timestamp windowStart);

    /**
     * A derived query to count non-flagged transactions for a specific account.
     * This will be used to update the average transaction amount correctly.
     *
     * @param accountId The account ID.
     * @param isFlagged The flag status to filter by (we will pass 'false').
     * @return The count of transactions matching the criteria.
     */
    long countByAccountIdAndIsFlagged(Integer accountId, boolean isFlagged);

}

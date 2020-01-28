package com.marqusm.example.moneytransaction.repository;

import com.marqusm.example.moneytransaction.exception.ConflictException;
import com.marqusm.example.moneytransaction.exception.NotFoundException;
import com.marqusm.example.moneytransaction.model.Transaction;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
public class TransactionRepository {
  private final Map<UUID, Transaction> transactionMap = new HashMap<>();

  public Transaction save(Transaction transaction) {
    if (transactionMap.containsKey(transaction.getId())) {
      throw new ConflictException("Transaction already exists: " + transaction.getId());
    } else {
      transactionMap.put(transaction.getId(), transaction);
      return transaction;
    }
  }

  public Transaction getById(UUID id) {
    return transactionMap.computeIfAbsent(
        id,
        key -> {
          throw new NotFoundException("Account not found: " + id);
        });
  }
}

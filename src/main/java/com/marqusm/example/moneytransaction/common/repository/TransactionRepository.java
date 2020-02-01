package com.marqusm.example.moneytransaction.common.repository;

import com.marqusm.example.moneytransaction.common.model.dto.Transaction;
import com.marqusm.example.moneytransaction.common.model.dto.TransactionCollection;
import java.util.Optional;
import java.util.UUID;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
public interface TransactionRepository {
  Optional<Transaction> findById(UUID id);

  TransactionCollection findByAccountId(UUID accountId);

  Transaction transferMoney(Transaction transaction);
}

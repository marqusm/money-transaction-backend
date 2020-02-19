package com.marqusm.example.moneytransaction.libimpl.vertxpg.repository;

import com.marqusm.example.moneytransaction.common.model.dto.Transaction;
import com.marqusm.example.moneytransaction.common.model.dto.TransactionCollection;
import com.marqusm.example.moneytransaction.common.repository.TransactionRepository;
import io.vertx.pgclient.PgPool;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Marko
 * @createdOn : 05-Feb-20
 */
@Slf4j
@AllArgsConstructor
public class VertxPgTransactionRepositoryImpl implements TransactionRepository {
  private final PgPool client;

  @Override
  public Optional<Transaction> findById(UUID id) {
    return Optional.empty();
  }

  @Override
  public TransactionCollection findByAccountId(UUID accountId) {
    return new TransactionCollection(null, 0);
  }

  @Override
  public Transaction transferMoney(Transaction transaction) {
    return new Transaction(null, null, null, null);
  }
}

package com.marqusm.example.moneytransaction.libimpl.inmemory.repository;

import com.marqusm.example.moneytransaction.common.exception.BadRequestException;
import com.marqusm.example.moneytransaction.common.exception.ConflictException;
import com.marqusm.example.moneytransaction.common.model.dto.Transaction;
import com.marqusm.example.moneytransaction.common.model.dto.TransactionCollection;
import com.marqusm.example.moneytransaction.common.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
public class InMemoryTransactionRepositoryImpl implements TransactionRepository {

  @Override
  public Optional<Transaction> findById(UUID transactionId) {
    return Optional.ofNullable(InMemoryStore.getTransactionMap().get(transactionId));
  }

  @Override
  public TransactionCollection findByAccountId(UUID accountId) {
    val count =
        InMemoryStore.getTransactionMap().values().stream()
            .filter(t -> t.getAccountId().equals(accountId))
            .count();
    return new TransactionCollection(null, count);
  }

  @Override
  public Transaction transferMoney(Transaction transaction) {
    transaction.setId(UUID.randomUUID());
    val ids = new ArrayList<UUID>(2);
    ids.add(InMemoryStore.getAccountMap().get(transaction.getAccountId()).getId());
    ids.add(InMemoryStore.getAccountMap().get(transaction.getRelatedAccountId()).getId());
    ids.sort(Comparator.naturalOrder());

    synchronized (ids.get(0)) {
      synchronized (ids.get(1)) {
        makeTransaction(transaction);
      }
    }

    if (InMemoryStore.getTransactionMap().containsKey(transaction.getId())) {
      throw new ConflictException("Transaction already exists: " + transaction.getId());
    } else {
      InMemoryStore.getTransactionMap().put(transaction.getId(), transaction);
      return transaction;
    }
  }

  protected void makeTransaction(Transaction transaction) {
    val primaryAccount = InMemoryStore.getAccountMap().get(transaction.getAccountId());
    val secondaryAccount = InMemoryStore.getAccountMap().get(transaction.getRelatedAccountId());

    if (primaryAccount.getBalance().add(transaction.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
      throw new BadRequestException("Transaction would make negative balance: " + transaction);
    }
    if (secondaryAccount.getBalance().subtract(transaction.getAmount()).compareTo(BigDecimal.ZERO)
        < 0) {
      throw new BadRequestException("Transaction would make negative balance: " + transaction);
    }

    primaryAccount.addAmount(transaction.getAmount());
    secondaryAccount.subtractAmount(transaction.getAmount());
  }
}

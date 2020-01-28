package com.marqusm.example.moneytransaction.service;

import com.google.inject.Inject;
import com.marqusm.example.moneytransaction.model.Transaction;
import com.marqusm.example.moneytransaction.repository.AccountRepository;
import com.marqusm.example.moneytransaction.repository.TransactionRepository;
import com.marqusm.example.moneytransaction.util.RepositoryUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 25-Jan-20
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;

  public Transaction createTransaction(Transaction transaction) {
    val newTransaction =
        new Transaction(
            UUID.randomUUID(),
            transaction.getAccountId(),
            transaction.getRelatedAccountId(),
            transaction.getAmount());
    newTransaction.setMetaActive(Boolean.TRUE);

    val ids = new ArrayList<UUID>(2);
    ids.add(transaction.getAccountId());
    ids.add(transaction.getRelatedAccountId());
    ids.sort(Comparator.naturalOrder());

    synchronized (ids.get(0)) {
      synchronized (ids.get(1)) {
        val primaryAccount = accountRepository.getById(transaction.getAccountId());
        val secondaryAccount = accountRepository.getById(transaction.getRelatedAccountId());

        if (primaryAccount.getAmount().add(transaction.getAmount()).compareTo(BigDecimal.ZERO)
            < 0) {
          throw new IllegalArgumentException(
              "Transaction would make negative balance: " + transaction);
        }
        if (secondaryAccount
                .getAmount()
                .subtract(transaction.getAmount())
                .compareTo(BigDecimal.ZERO)
            < 0) {
          throw new IllegalArgumentException(
              "Transaction would make negative balance: " + transaction);
        }

        primaryAccount.addAmount(transaction.getAmount());
        secondaryAccount.subtractAmount(transaction.getAmount());
      }
    }

    return transactionRepository.save(newTransaction);
  }

  public Transaction getTransaction(UUID transactionId) {
    val transaction = transactionRepository.getById(transactionId);
    RepositoryUtil.checkIfExists(transaction, "Transaction not found: " + transactionId);
    return transaction;
  }
}

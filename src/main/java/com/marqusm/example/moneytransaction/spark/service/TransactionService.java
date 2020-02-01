package com.marqusm.example.moneytransaction.spark.service;

import com.google.inject.Inject;
import com.marqusm.example.moneytransaction.common.model.dto.Transaction;
import com.marqusm.example.moneytransaction.common.model.dto.TransactionCollection;
import com.marqusm.example.moneytransaction.spark.repository.AccountRepository;
import com.marqusm.example.moneytransaction.spark.repository.TransactionRepository;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 25-Jan-20
 */
@Slf4j
@AllArgsConstructor(access = AccessLevel.PACKAGE, onConstructor = @__({@Inject}))
public class TransactionService {

  private final TransactionRepository transactionRepository;
  protected final AccountRepository accountRepository;

  public Transaction createTransaction(Transaction transaction) {
    val newTransaction =
        new Transaction(
            null,
            transaction.getAccountId(),
            transaction.getRelatedAccountId(),
            transaction.getAmount());
    return transactionRepository.transferMoney(transaction);
  }

  public Transaction getTransaction(UUID transactionId) {
    return transactionRepository.getById(transactionId);
  }

  public TransactionCollection getTransactionByAccountId(UUID accountId) {
    return transactionRepository.getByAccountId(accountId);
  }
}

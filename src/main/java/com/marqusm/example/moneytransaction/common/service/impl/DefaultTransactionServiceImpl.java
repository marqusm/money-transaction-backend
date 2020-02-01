package com.marqusm.example.moneytransaction.common.service.impl;

import com.marqusm.example.moneytransaction.common.exception.NotFoundException;
import com.marqusm.example.moneytransaction.common.model.dto.Transaction;
import com.marqusm.example.moneytransaction.common.model.dto.TransactionCollection;
import com.marqusm.example.moneytransaction.common.repository.AccountRepository;
import com.marqusm.example.moneytransaction.common.repository.TransactionRepository;
import com.marqusm.example.moneytransaction.common.service.TransactionService;
import java.util.UUID;
import lombok.AllArgsConstructor;

/**
 * @author : Marko
 * @createdOn : 25-Jan-20
 */
@AllArgsConstructor
public class DefaultTransactionServiceImpl implements TransactionService {

  protected final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;

  public Transaction createTransaction(Transaction transaction) {
    return transactionRepository.transferMoney(transaction);
  }

  public Transaction getTransaction(UUID transactionId) {
    return transactionRepository
        .findById(transactionId)
        .orElseThrow(() -> new NotFoundException("Transaction not found: " + transactionId));
  }

  public TransactionCollection getTransactionByAccountId(UUID accountId) {
    return transactionRepository.findByAccountId(accountId);
  }
}

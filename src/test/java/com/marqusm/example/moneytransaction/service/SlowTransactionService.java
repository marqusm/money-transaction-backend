package com.marqusm.example.moneytransaction.service;

import com.marqusm.example.moneytransaction.common.exception.NotFoundException;
import com.marqusm.example.moneytransaction.common.model.dto.Transaction;
import com.marqusm.example.moneytransaction.common.repository.AccountRepository;
import com.marqusm.example.moneytransaction.common.repository.TransactionRepository;
import com.marqusm.example.moneytransaction.common.service.impl.DefaultTransactionServiceImpl;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;
import lombok.SneakyThrows;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 29-Jan-20
 */
public class SlowTransactionService extends DefaultTransactionServiceImpl {
  public SlowTransactionService(
      AccountRepository accountRepository, TransactionRepository transactionRepository) {
    super(accountRepository, transactionRepository);
  }

  @SneakyThrows
  @Override
  public Transaction createTransaction(Transaction transaction) {
    val ids = new ArrayList<UUID>(2);
    ids.add(
        accountRepository
            .findById(transaction.getAccountId())
            .orElseThrow(() -> new NotFoundException("Not found"))
            .getId());
    ids.add(
        accountRepository
            .findById(transaction.getRelatedAccountId())
            .orElseThrow(() -> new NotFoundException("Not found"))
            .getId());
    ids.sort(Comparator.naturalOrder());
    ids.get(1).wait(500);
    return super.createTransaction(transaction);
  }
}

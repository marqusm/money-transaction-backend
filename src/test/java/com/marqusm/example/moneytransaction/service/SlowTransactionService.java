package com.marqusm.example.moneytransaction.service;

import com.google.inject.Inject;
import com.marqusm.example.moneytransaction.model.Transaction;
import com.marqusm.example.moneytransaction.repository.AccountRepository;
import com.marqusm.example.moneytransaction.repository.TransactionRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;
import lombok.SneakyThrows;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 29-Jan-20
 */
public class SlowTransactionService extends TransactionService {
  @Inject
  public SlowTransactionService(
      TransactionRepository transactionRepository, AccountRepository accountRepository) {
    super(transactionRepository, accountRepository);
  }

  @SneakyThrows
  @Override
  protected void makeTransaction(Transaction transaction) {
    val ids = new ArrayList<UUID>(2);
    ids.add(accountRepository.getById(transaction.getAccountId()).getId());
    ids.add(accountRepository.getById(transaction.getRelatedAccountId()).getId());
    ids.sort(Comparator.naturalOrder());
    ids.get(1).wait(500);
    super.makeTransaction(transaction);
  }
}

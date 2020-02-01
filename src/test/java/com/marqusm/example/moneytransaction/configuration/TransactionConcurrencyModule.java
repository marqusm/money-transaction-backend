package com.marqusm.example.moneytransaction.configuration;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.marqusm.example.moneytransaction.common.repository.AccountRepository;
import com.marqusm.example.moneytransaction.common.repository.TransactionRepository;
import com.marqusm.example.moneytransaction.common.service.TransactionService;
import com.marqusm.example.moneytransaction.service.SlowTransactionService;

/**
 * @author : Marko
 * @createdOn : 29-Jan-20
 */
public class TransactionConcurrencyModule implements Module {
  @Override
  public void configure(Binder binder) {}

  @Provides
  @Singleton
  TransactionService provideTransactionService(
      AccountRepository accountRepository, TransactionRepository transactionRepository) {
    return new SlowTransactionService(accountRepository, transactionRepository);
  }
}

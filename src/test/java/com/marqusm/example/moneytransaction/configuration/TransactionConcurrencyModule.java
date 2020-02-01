package com.marqusm.example.moneytransaction.configuration;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.marqusm.example.moneytransaction.spark.service.SlowTransactionService;
import com.marqusm.example.moneytransaction.spark.service.TransactionService;

/**
 * @author : Marko
 * @createdOn : 29-Jan-20
 */
public class TransactionConcurrencyModule implements Module {
  public void configure(Binder binder) {
    binder.bind(TransactionService.class).to(SlowTransactionService.class).in(Singleton.class);
  }
}

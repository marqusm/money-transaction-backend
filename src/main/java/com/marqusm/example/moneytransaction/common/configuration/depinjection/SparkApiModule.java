package com.marqusm.example.moneytransaction.common.configuration.depinjection;

import com.google.gson.Gson;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.marqusm.example.moneytransaction.common.service.AccountService;
import com.marqusm.example.moneytransaction.common.service.TransactionService;
import com.marqusm.example.moneytransaction.controller.SparkAccountController;
import com.marqusm.example.moneytransaction.controller.SparkTransactionController;
import com.marqusm.example.moneytransaction.libimpl.spark.transformer.JsonGsonTransformer;
import lombok.NoArgsConstructor;
import spark.ResponseTransformer;

/**
 * @author : Marko
 * @createdOn : 25-Jan-20
 */
@NoArgsConstructor
public class SparkApiModule implements Module {
  @Override
  public void configure(Binder binder) {}

  @Provides
  @Singleton
  ResponseTransformer provideResponseTransformer(Gson gson) {
    return new JsonGsonTransformer(gson);
  }

  @Provides
  @Singleton
  SparkAccountController provideAccountController(Gson gson, AccountService accountService) {
    return new SparkAccountController(gson, accountService);
  }

  @Provides
  @Singleton
  SparkTransactionController provideTransactionController(
      Gson gson, TransactionService transactionService) {
    return new SparkTransactionController(gson, transactionService);
  }
}

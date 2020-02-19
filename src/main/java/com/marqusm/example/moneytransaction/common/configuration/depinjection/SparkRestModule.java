package com.marqusm.example.moneytransaction.common.configuration.depinjection;

import com.google.gson.Gson;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.marqusm.example.moneytransaction.common.rest.RestStarter;
import com.marqusm.example.moneytransaction.common.service.AccountService;
import com.marqusm.example.moneytransaction.common.service.TransactionService;
import com.marqusm.example.moneytransaction.libimpl.spark.controller.SparkAccountController;
import com.marqusm.example.moneytransaction.libimpl.spark.controller.SparkTransactionController;
import com.marqusm.example.moneytransaction.libimpl.spark.filter.ContentTypeFilter;
import com.marqusm.example.moneytransaction.libimpl.spark.rest.SparkRestStarter;
import com.marqusm.example.moneytransaction.libimpl.spark.transformer.JsonGsonTransformer;
import lombok.NoArgsConstructor;
import spark.ResponseTransformer;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
@NoArgsConstructor
public class SparkRestModule implements Module {
  @Override
  public void configure(Binder binder) {}

  @Provides
  @Singleton
  RestStarter provideApiConfig(
      SparkAccountController accountController,
      SparkTransactionController sparkTransactionController,
      ResponseTransformer responseTransformer,
      Gson gson) {
    return new SparkRestStarter(
        accountController,
        sparkTransactionController,
        responseTransformer,
        gson,
        new ContentTypeFilter());
  }

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

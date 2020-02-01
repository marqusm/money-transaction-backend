package com.marqusm.example.moneytransaction.common.configuration.depinjection;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.marqusm.example.moneytransaction.common.annotation.JsonExclude;
import com.marqusm.example.moneytransaction.common.repository.AccountRepository;
import com.marqusm.example.moneytransaction.common.repository.TransactionRepository;
import com.marqusm.example.moneytransaction.common.service.AccountService;
import com.marqusm.example.moneytransaction.common.service.TransactionService;
import com.marqusm.example.moneytransaction.common.service.impl.DefaultAccountServiceImpl;
import com.marqusm.example.moneytransaction.common.service.impl.DefaultTransactionServiceImpl;
import lombok.NoArgsConstructor;

/**
 * @author : Marko
 * @createdOn : 25-Jan-20
 */
@NoArgsConstructor
public class DefaultModule implements Module {
  @Override
  public void configure(Binder binder) {}

  @Provides
  @Singleton
  AccountService provideAccountService(AccountRepository accountRepository) {
    return new DefaultAccountServiceImpl(accountRepository);
  }

  @Provides
  @Singleton
  TransactionService provideTransactionService(
      AccountRepository accountRepository, TransactionRepository transactionRepository) {
    return new DefaultTransactionServiceImpl(accountRepository, transactionRepository);
  }

  @Provides
  @Singleton
  Gson provideGson() {
    ExclusionStrategy strategy =
        new ExclusionStrategy() {
          @Override
          public boolean shouldSkipField(FieldAttributes field) {
            return field.getAnnotation(JsonExclude.class) != null;
          }

          @Override
          public boolean shouldSkipClass(Class<?> clazz) {
            return false;
          }
        };
    return new GsonBuilder().addSerializationExclusionStrategy(strategy).create();
  }
}

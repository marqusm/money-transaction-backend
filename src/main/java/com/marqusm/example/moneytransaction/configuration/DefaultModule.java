package com.marqusm.example.moneytransaction.configuration;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.marqusm.example.moneytransaction.controller.AccountController;
import com.marqusm.example.moneytransaction.controller.TransactionController;
import com.marqusm.example.moneytransaction.repository.AccountRepository;
import com.marqusm.example.moneytransaction.repository.TransactionRepository;
import com.marqusm.example.moneytransaction.service.AccountService;
import com.marqusm.example.moneytransaction.service.TransactionService;
import com.marqusm.example.moneytransaction.transformer.JsonGsonTransformer;
import lombok.NoArgsConstructor;
import spark.ResponseTransformer;

/**
 * @author : Marko
 * @createdOn : 25-Jan-20
 */
@NoArgsConstructor
public class DefaultModule implements Module {
  public void configure(Binder binder) {
    binder.bind(ResponseTransformer.class).to(JsonGsonTransformer.class);

    binder.bind(AccountRepository.class).in(Singleton.class);
    binder.bind(TransactionRepository.class).in(Singleton.class);
    binder.bind(AccountService.class).in(Singleton.class);
    binder.bind(TransactionService.class).in(Singleton.class);
    binder.bind(AccountController.class).in(Singleton.class);
    binder.bind(TransactionController.class).in(Singleton.class);
  }
}

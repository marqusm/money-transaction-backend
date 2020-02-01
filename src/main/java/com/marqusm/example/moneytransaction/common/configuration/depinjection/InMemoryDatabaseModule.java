package com.marqusm.example.moneytransaction.common.configuration.depinjection;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.marqusm.example.moneytransaction.common.repository.AccountRepository;
import com.marqusm.example.moneytransaction.common.repository.TransactionRepository;
import com.marqusm.example.moneytransaction.libimpl.inmemory.repository.InMemoryAccountRepositoryImpl;
import com.marqusm.example.moneytransaction.libimpl.inmemory.repository.InMemoryTransactionRepositoryImpl;
import lombok.NoArgsConstructor;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
@NoArgsConstructor
public class InMemoryDatabaseModule implements Module {
  @Override
  public void configure(Binder binder) {
    binder
        .bind(AccountRepository.class)
        .to(InMemoryAccountRepositoryImpl.class)
        .in(Singleton.class);
    binder
        .bind(TransactionRepository.class)
        .to(InMemoryTransactionRepositoryImpl.class)
        .in(Singleton.class);
  }
}

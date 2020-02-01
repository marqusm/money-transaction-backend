package com.marqusm.example.moneytransaction.libimpl.inmemory.repository;

import com.marqusm.example.moneytransaction.common.exception.ConflictException;
import com.marqusm.example.moneytransaction.common.model.dto.Account;
import com.marqusm.example.moneytransaction.common.repository.AccountRepository;
import java.util.Optional;
import java.util.UUID;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
public class InMemoryAccountRepositoryImpl implements AccountRepository {

  @Override
  public Account save(Account account) {
    if (InMemoryStore.getAccountMap().containsKey(account.getId())) {
      throw new ConflictException("Account already exists: " + account.getId());
    } else {
      InMemoryStore.getAccountMap().put(account.getId(), account);
      return account;
    }
  }

  @Override
  public Optional<Account> findById(UUID accountId) {
    return Optional.ofNullable(InMemoryStore.getAccountMap().get(accountId));
  }

  @Override
  public void inactivate(UUID accountId) {
    InMemoryStore.getAccountMap().remove(accountId);
  }
}

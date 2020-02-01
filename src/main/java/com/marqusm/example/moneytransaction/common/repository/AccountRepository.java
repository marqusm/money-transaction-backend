package com.marqusm.example.moneytransaction.common.repository;

import com.marqusm.example.moneytransaction.common.model.dto.Account;
import java.util.Optional;
import java.util.UUID;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
public interface AccountRepository {
  Account save(Account account);

  Optional<Account> findById(UUID id);

  void inactivate(UUID id);
}

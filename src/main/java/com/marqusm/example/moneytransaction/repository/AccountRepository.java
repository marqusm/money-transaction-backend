package com.marqusm.example.moneytransaction.repository;

import com.marqusm.example.moneytransaction.exception.ConflictException;
import com.marqusm.example.moneytransaction.exception.NotFoundException;
import com.marqusm.example.moneytransaction.model.Account;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
public class AccountRepository {
  private final Map<UUID, Account> accountMap = new HashMap<>();

  public Account save(Account account) {
    if (accountMap.containsKey(account.getId())) {
      throw new ConflictException("Account already exists: " + account.getId());
    } else {
      accountMap.put(account.getId(), account);
      return account;
    }
  }

  public Account getById(UUID id) {
    return accountMap.computeIfAbsent(
        id,
        key -> {
          throw new NotFoundException("Account not found: " + id);
        });
  }

  public Account update(Account accountRecord) {
    accountMap.put(accountRecord.getId(), accountRecord);
    return accountRecord;
  }
}

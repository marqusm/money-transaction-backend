package com.marqusm.example.moneytransaction.common.service;

import com.marqusm.example.moneytransaction.common.model.dto.Account;
import java.util.UUID;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
public interface AccountService {
  Account createAccount(Account request);

  Account getAccount(UUID accountId);

  Void deleteAccount(UUID accountId);
}

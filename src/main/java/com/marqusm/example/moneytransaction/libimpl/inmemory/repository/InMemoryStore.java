package com.marqusm.example.moneytransaction.libimpl.inmemory.repository;

import com.marqusm.example.moneytransaction.common.model.dto.Account;
import com.marqusm.example.moneytransaction.common.model.dto.Transaction;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
@Getter
public class InMemoryStore {
  @Getter private static final Map<UUID, Account> accountMap = new HashMap<>();
  @Getter private static final Map<UUID, Transaction> transactionMap = new HashMap<>();
}

package com.marqusm.example.moneytransaction;

import com.marqusm.example.moneytransaction.constant.ApiPath;
import com.marqusm.example.moneytransaction.model.Account;
import com.marqusm.example.moneytransaction.model.Transaction;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
public class TestData {
  public static final String API_PREFIX = "http://localhost:4567" + ApiPath.API_PREFIX;

  public static Account createAccount() {
    val account = new Account(UUID.randomUUID(), BigDecimal.ONE);
    account.setMetaActive(Boolean.TRUE);
    return account;
  }

  public static Transaction createTransaction() {
    val transaction =
        new Transaction(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), BigDecimal.ONE);
    transaction.setMetaActive(Boolean.TRUE);
    return transaction;
  }
}

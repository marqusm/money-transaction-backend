package com.marqusm.example.moneytransaction.controller;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.marqusm.example.moneytransaction.model.Transaction;
import com.marqusm.example.moneytransaction.service.TransactionService;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;
import spark.Route;

/**
 * @author : Marko
 * @createdOn : 25-Jan-20
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public class TransactionController {

  private final TransactionService transactionService;
  private final Gson gson;

  public Route create() {
    return (request, response) -> {
      val requestTransaction = gson.fromJson(request.body(), Transaction.class);
      val transaction =
          new Transaction(
              null,
              UUID.fromString(request.params("accountId")),
              requestTransaction.getRelatedAccountId(),
              requestTransaction.getAmount());
      return transactionService.createTransaction(transaction);
    };
  }

  public Route readById() {
    return (request, response) ->
        transactionService.getTransaction(UUID.fromString(request.params(":transactionId")));
  }
}

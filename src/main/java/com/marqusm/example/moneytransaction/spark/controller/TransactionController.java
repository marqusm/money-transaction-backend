package com.marqusm.example.moneytransaction.spark.controller;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.marqusm.example.moneytransaction.common.model.dto.Transaction;
import com.marqusm.example.moneytransaction.spark.service.TransactionService;
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

  private final Gson gson;
  private final TransactionService transactionService;

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

  public Route readByAccountId() {
    return (request, response) ->
        transactionService.getTransactionByAccountId(UUID.fromString(request.params(":accountId")));
  }
}
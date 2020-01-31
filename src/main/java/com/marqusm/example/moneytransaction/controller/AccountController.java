package com.marqusm.example.moneytransaction.controller;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.marqusm.example.moneytransaction.model.dto.Account;
import com.marqusm.example.moneytransaction.service.AccountService;
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
public class AccountController {

  private final Gson gson;
  private final AccountService accountService;

  public Route create() {
    return (request, response) -> {
      val account = gson.fromJson(request.body(), Account.class);
      return accountService.createAccount(account);
    };
  }

  public Route read() {
    return (request, response) ->
        accountService.getAccount(UUID.fromString(request.params(":accountId")));
  }

  public Route delete() {
    return (request, response) -> {
      accountService.deleteAccount(UUID.fromString(request.params(":accountId")));
      return null;
    };
  }
}

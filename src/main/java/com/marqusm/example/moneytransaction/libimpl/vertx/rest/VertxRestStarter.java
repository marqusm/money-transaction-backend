package com.marqusm.example.moneytransaction.libimpl.vertx.rest;

import com.google.gson.Gson;
import com.marqusm.example.moneytransaction.common.rest.RestStarter;
import com.marqusm.example.moneytransaction.libimpl.vertx.controller.AccountController;
import com.marqusm.example.moneytransaction.libimpl.vertxpg.repository.VertxAccountRepository;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
@Slf4j
@AllArgsConstructor
public class VertxRestStarter implements RestStarter {

  private final Gson gson;
  private final AccountController accountController;
  private final VertxAccountRepository accountRepository;

  @Override
  public void startRest() {
    val vertx = Vertx.vertx();
    val restOptions = new DeploymentOptions();
    //    vertx.deployVerticle(() -> new VertxPgVerticle(gson, accountRepository), restOptions);
    vertx.deployVerticle(() -> new VertxRestVerticle(accountController), restOptions);
  }

  @Override
  public void stopRest() {}
}

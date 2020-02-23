package com.marqusm.example.moneytransaction.common.configuration.depinjection;

import com.google.gson.Gson;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.marqusm.example.moneytransaction.common.rest.RestStarter;
import com.marqusm.example.moneytransaction.libimpl.vertx.controller.AccountController;
import com.marqusm.example.moneytransaction.libimpl.vertx.rest.VertxRestStarter;
import com.marqusm.example.moneytransaction.libimpl.vertx.util.RoutingContextUtil;
import com.marqusm.example.moneytransaction.libimpl.vertx.validator.Validator;
import com.marqusm.example.moneytransaction.libimpl.vertxpg.repository.VertxAccountRepository;
import io.vertx.pgclient.PgPool;
import lombok.NoArgsConstructor;
import org.jooq.DSLContext;

/**
 * @author : Marko
 * @createdOn : 04-Feb-20
 */
@NoArgsConstructor
public class VertxRestModule implements Module {
  @Override
  public void configure(Binder binder) {}

  @Provides
  @Singleton
  RestStarter provideRestStarter(
      Gson gson, AccountController accountController, VertxAccountRepository accountRepository) {
    return new VertxRestStarter(gson, accountController, accountRepository);
  }

  @Provides
  @Singleton
  AccountController provideAccountController(
      Gson gson,
      PgPool pgPool,
      DSLContext dslContext,
      RoutingContextUtil routingContextUtil,
      Validator validator) {
    return new AccountController(gson, pgPool, dslContext, routingContextUtil, validator);
  }

  @Provides
  @Singleton
  RoutingContextUtil provideRoutingContextUtil(Gson gson) {
    return new RoutingContextUtil(gson);
  }

  @Provides
  @Singleton
  Validator provideValidator(Gson gson, RoutingContextUtil routingContextUtil) {
    return new Validator(gson, routingContextUtil);
  }
}

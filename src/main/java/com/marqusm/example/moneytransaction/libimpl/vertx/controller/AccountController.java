package com.marqusm.example.moneytransaction.libimpl.vertx.controller;

import static com.marqusm.example.moneytransaction.common.model.generated.jooq.Tables.ACCOUNT;

import com.google.gson.Gson;
import com.marqusm.example.moneytransaction.common.constant.SqlQuery;
import com.marqusm.example.moneytransaction.common.model.dto.Account;
import com.marqusm.example.moneytransaction.libimpl.vertx.constant.MessageAddress;
import com.marqusm.example.moneytransaction.libimpl.vertx.util.RoutingContextUtil;
import com.marqusm.example.moneytransaction.libimpl.vertx.validator.Validator;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jooq.DSLContext;

/**
 * @author : Marko
 * @createdOn : 08-Feb-20
 */
@Slf4j
@AllArgsConstructor
public class AccountController {

  private final Gson gson;
  private final PgPool pgPool;
  private final DSLContext create;
  private final RoutingContextUtil routingContextUtil;
  private final Validator validator;

  public Handler<RoutingContext> createAccountHandler(Vertx vertx) {
    return request -> {
      validator.validate(request.getBodyAsString(), Account.class, request);
      Promise<Message<String>> promise = Promise.promise();
      vertx
          .eventBus()
          .request(MessageAddress.CREATE_ACCOUNT.name(), request.getBodyAsString(), promise);
      promise
          .future()
          .onSuccess(msgResponse -> routingContextUtil.fillSuccessfulResponse(request, msgResponse))
          .onFailure(
              cause -> routingContextUtil.onFailure(request, "Create account call failed.", cause));
    };
  }

  public Handler<RoutingContext> findAccountByIdHandler(Vertx vertx) {
    return request -> {
      val accountId = request.pathParam("accountId");
      Promise<Optional<Account>> promise = Promise.promise();

      final Promise<RowSet<Row>> queryPromise = Promise.promise();
      pgPool.preparedQuery(
          SqlQuery.READ_ACCOUNT_BY_ID, Tuple.of(UUID.fromString(accountId)), queryPromise);
      queryPromise
          .future()
          .onSuccess(
              rowSet -> {
                Optional<Account> result;
                if (rowSet.size() == 0) {
                  result = Optional.empty();
                } else {
                  val row = rowSet.iterator().next();
                  result =
                      Optional.of(
                          new Account(
                              row.getUUID(ACCOUNT.ACCOUNT_ID.getName()),
                              row.getBigDecimal(ACCOUNT.BALANCE.getName())));
                }
                promise.complete(result);
              })
          .onFailure(
              cause -> {
                onDatabaseActionFailed(SqlQuery.READ_ACCOUNT_BY_ID, null, cause, promise);
              });
      promise
          .future()
          .onSuccess(
              msgResponse -> {
                routingContextUtil.fillSuccessfulResponse(
                    request, msgResponse.map(gson::toJson).orElse("{}"));
              })
          .onFailure(
              cause ->
                  routingContextUtil.onFailure(request, "Find account by id call failed.", cause));
    };
  }

  public Handler<RoutingContext> deleteAccountByIdHandler(Vertx vertx) {
    return request -> {
      val accountId = request.pathParam("accountId");
      Promise<Message<String>> promise = Promise.promise();
      vertx.eventBus().request(MessageAddress.DELETE_ACCOUNT_BY_ID.name(), accountId, promise);
      promise
          .future()
          .onSuccess(msgResponse -> routingContextUtil.fillSuccessfulResponse(request, msgResponse))
          .onFailure(
              cause ->
                  routingContextUtil.onFailure(
                      request, "Delete account by id call failed.", cause));
    };
  }

  private void onDatabaseActionFailed(
      String query, SqlConnection conn, Throwable cause, Promise promise) {
    log.error("Database action failed: " + query, cause);
    if (conn != null) {
      conn.close();
    }
    promise.fail(cause);
  }
}

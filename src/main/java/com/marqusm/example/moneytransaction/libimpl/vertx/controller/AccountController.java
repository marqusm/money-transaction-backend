package com.marqusm.example.moneytransaction.libimpl.vertx.controller;

import com.marqusm.example.moneytransaction.common.model.dto.Account;
import com.marqusm.example.moneytransaction.libimpl.vertx.constant.MessageAddress;
import com.marqusm.example.moneytransaction.libimpl.vertx.util.RoutingContextUtil;
import com.marqusm.example.moneytransaction.libimpl.vertx.validator.Validator;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.web.RoutingContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 08-Feb-20
 */
@Slf4j
@AllArgsConstructor
public class AccountController {

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
      Promise<Message<String>> promise = Promise.promise();
      vertx.eventBus().request(MessageAddress.FIND_ACCOUNT_BY_ID.name(), accountId, promise);
      promise
          .future()
          .onSuccess(msgResponse -> routingContextUtil.fillSuccessfulResponse(request, msgResponse))
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
}

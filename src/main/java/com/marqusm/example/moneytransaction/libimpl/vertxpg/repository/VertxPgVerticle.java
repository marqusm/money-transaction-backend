package com.marqusm.example.moneytransaction.libimpl.vertxpg.repository;

import com.google.gson.Gson;
import com.marqusm.example.moneytransaction.common.model.dto.Account;
import com.marqusm.example.moneytransaction.libimpl.vertx.constant.MessageAddress;
import com.marqusm.example.moneytransaction.libimpl.vertx.util.DeliveryOptionsUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 06-Feb-20
 */
@Slf4j
@AllArgsConstructor
public class VertxPgVerticle extends AbstractVerticle {

  private final Gson gson;
  private final VertxAccountRepository accountRepository;

  @Override
  public void start(Promise<Void> promise) {
    vertx.eventBus().consumer(MessageAddress.CREATE_ACCOUNT.name(), this::onCreateAccountMessage);
    vertx
        .eventBus()
        .consumer(MessageAddress.FIND_ACCOUNT_BY_ID.name(), this::onFindAccountByIdMessage);
    vertx
        .eventBus()
        .consumer(MessageAddress.DELETE_ACCOUNT_BY_ID.name(), this::onDeleteAccountByIdMessage);
    promise.complete();
  }

  private void onCreateAccountMessage(Message<String> message) {
    Promise<Account> promise = Promise.promise();
    accountRepository.save(gson.fromJson(message.body(), Account.class), promise);
    promise
        .future()
        .onSuccess(account -> message.reply(gson.toJson(account)))
        .onFailure(cause -> message.fail(1, cause.toString()));
  }

  private void onFindAccountByIdMessage(Message<String> message) {
    val accountId = UUID.fromString(message.body());
    Promise<Optional<Account>> promise = Promise.promise();
    accountRepository.findById(accountId, promise);
    promise
        .future()
        .onSuccess(
            optAccount ->
                optAccount.ifPresentOrElse(
                    account -> message.reply(gson.toJson(account)),
                    () ->
                        message.reply(
                            "Account not found: " + accountId,
                            DeliveryOptionsUtil.createErrorDeliveryOptions(404))))
        .onFailure(cause -> message.fail(1, cause.toString()));
  }

  private void onDeleteAccountByIdMessage(Message<String> message) {
    val accountId = UUID.fromString(message.body());
    Promise<Void> promise = Promise.promise();
    accountRepository.inactivate(accountId, promise);
    promise
        .future()
        .onSuccess(result -> message.reply(null))
        .onFailure(cause -> message.fail(1, cause.toString()));
  }
}

package com.marqusm.example.moneytransaction.libimpl.spark.rest;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.marqusm.example.moneytransaction.common.constant.ApiPath;
import com.marqusm.example.moneytransaction.common.constant.ContentTypeName;
import com.marqusm.example.moneytransaction.common.constant.HttpHeaderName;
import com.marqusm.example.moneytransaction.common.exception.base.ClientHttpException;
import com.marqusm.example.moneytransaction.common.model.dto.ErrorResponse;
import com.marqusm.example.moneytransaction.common.rest.RestStarter;
import com.marqusm.example.moneytransaction.libimpl.spark.controller.SparkAccountController;
import com.marqusm.example.moneytransaction.libimpl.spark.controller.SparkTransactionController;
import com.marqusm.example.moneytransaction.libimpl.spark.filter.ContentTypeFilter;
import com.marqusm.example.moneytransaction.libimpl.spark.util.HttpRequestUtils;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import spark.ResponseTransformer;

/**
 * @author : Marko
 * @createdOn : 04-Feb-20
 */
@Slf4j
@AllArgsConstructor
public class SparkRestStarter implements RestStarter {

  private final SparkAccountController accountController;
  private final SparkTransactionController transactionController;
  private final ResponseTransformer responseTransformer;
  private final Gson gson;
  private final ContentTypeFilter contentTypeFilter;

  @Override
  public void startRest() {
    path(
        ApiPath.API_PREFIX,
        () -> {
          before(
              "/*",
              (request, response) -> {
                val currRequestId = UUID.randomUUID().toString();
                log.info(HttpRequestUtils.toPrettyString(request, currRequestId));
                response.header(HttpHeaderName.X_Request_ID, currRequestId);
                contentTypeFilter.apply(request);
                response.type(ContentTypeName.APPLICATION_JSON);
              });
          afterAfter(
              "/*", (request, response) -> log.info(HttpRequestUtils.toPrettyString(response)));
          exception(
              ClientHttpException.class,
              (exception, request, response) -> {
                log.error(HttpRequestUtils.toPrettyExceptionString(request, response), exception);
                response.status(exception.getStatusCode());
                response.type(ContentTypeName.APPLICATION_JSON);
                response.body(gson.toJson(new ErrorResponse(exception.getMessage())));
              });
          exception(
              JsonSyntaxException.class,
              (exception, request, response) -> {
                log.error(HttpRequestUtils.toPrettyExceptionString(request, response), exception);
                response.status(400);
                response.type(ContentTypeName.APPLICATION_JSON);
                response.body(gson.toJson(new ErrorResponse("Illegal body data.")));
              });
          exception(
              NoSuchElementException.class,
              (exception, request, response) -> {
                log.error(HttpRequestUtils.toPrettyExceptionString(request, response), exception);
                response.status(404);
                response.type(ContentTypeName.APPLICATION_JSON);
                response.body(gson.toJson(new ErrorResponse("Resource not found. ")));
              });
          exception(
              Exception.class,
              (exception, request, response) -> {
                log.error(HttpRequestUtils.toPrettyExceptionString(request, response), exception);
                response.status(500);
                response.type(ContentTypeName.APPLICATION_JSON);
                response.body(
                    gson.toJson(new ErrorResponse("Internal error. " + exception.toString())));
              });
          path(
              ApiPath.ACCOUNTS,
              () -> {
                post(
                    "",
                    ContentTypeName.APPLICATION_JSON,
                    accountController.create(),
                    responseTransformer);
                get("/:accountId", accountController.read(), responseTransformer);
                delete("/:accountId", accountController.delete(), responseTransformer);
              });
          path(
              ApiPath.ACCOUNTS_TRANSACTIONS,
              () -> {
                post(
                    "",
                    ContentTypeName.APPLICATION_JSON,
                    transactionController.create(),
                    responseTransformer);
                get("/:transactionId", transactionController.readById(), responseTransformer);
                get("", transactionController.readByAccountId(), responseTransformer);
              });
          path(
              ApiPath.TRANSACTIONS,
              () -> get("/:transactionId", transactionController.readById(), responseTransformer));
        });

    awaitInitialization();
  }

  @Override
  public void stopRest() {
    stop();
    awaitStop();
  }
}

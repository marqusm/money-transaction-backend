package com.marqusm.example.moneytransaction.spark.configuration;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.marqusm.example.moneytransaction.common.constant.ApiPath;
import com.marqusm.example.moneytransaction.common.constant.ContentTypeName;
import com.marqusm.example.moneytransaction.common.constant.HttpHeaderName;
import com.marqusm.example.moneytransaction.common.exception.base.ClientHttpException;
import com.marqusm.example.moneytransaction.common.model.dto.ErrorResponse;
import com.marqusm.example.moneytransaction.spark.controller.AccountController;
import com.marqusm.example.moneytransaction.spark.controller.TransactionController;
import com.marqusm.example.moneytransaction.spark.filter.ContentTypeFilter;
import com.marqusm.example.moneytransaction.spark.util.HttpRequestUtils;
import java.util.NoSuchElementException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import spark.ResponseTransformer;

/**
 * @author : Marko
 * @createdOn : 25-Jan-20
 */
@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public class ApiConfig {

  private final AccountController accountController;
  private final TransactionController transactionController;
  private final ResponseTransformer responseTransformer;
  private final Gson gson;
  private final ContentTypeFilter contentTypeFilter;

  private static int requestId = 1;

  public void establishApi() {
    path(
        ApiPath.API_PREFIX,
        () -> {
          before(
              "/*",
              (request, response) -> {
                val currRequestId = requestId++ + "";
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
  }
}

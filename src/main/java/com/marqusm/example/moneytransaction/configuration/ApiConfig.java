package com.marqusm.example.moneytransaction.configuration;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.marqusm.example.moneytransaction.constant.ApiPath;
import com.marqusm.example.moneytransaction.constant.ContentTypeName;
import com.marqusm.example.moneytransaction.constant.HttpHeaderName;
import com.marqusm.example.moneytransaction.controller.AccountController;
import com.marqusm.example.moneytransaction.controller.TransactionController;
import com.marqusm.example.moneytransaction.exception.BadRequestException;
import com.marqusm.example.moneytransaction.exception.ConflictException;
import com.marqusm.example.moneytransaction.exception.NotAcceptableException;
import com.marqusm.example.moneytransaction.exception.NotFoundException;
import com.marqusm.example.moneytransaction.filter.ContentTypeFilter;
import com.marqusm.example.moneytransaction.model.ErrorResponse;
import com.marqusm.example.moneytransaction.util.HttpRequestUtils;
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
              BadRequestException.class,
              (exception, request, response) -> {
                log.error(HttpRequestUtils.toPrettyExceptionString(request, response), exception);
                response.status(400);
                response.type(ContentTypeName.APPLICATION_JSON);
                response.body(gson.toJson(new ErrorResponse(exception.getMessage())));
              });
          exception(
              ConflictException.class,
              (exception, request, response) -> {
                log.error(HttpRequestUtils.toPrettyExceptionString(request, response), exception);
                response.status(409);
                response.type(ContentTypeName.APPLICATION_JSON);
                response.body(gson.toJson(new ErrorResponse(exception.getMessage())));
              });
          exception(
              NotAcceptableException.class,
              (exception, request, response) -> {
                log.error(HttpRequestUtils.toPrettyExceptionString(request, response), exception);
                response.status(406);
                response.type(ContentTypeName.APPLICATION_JSON);
                response.body(gson.toJson(new ErrorResponse(exception.getMessage())));
              });
          exception(
              NotFoundException.class,
              (exception, request, response) -> {
                log.error(HttpRequestUtils.toPrettyExceptionString(request, response), exception);
                response.status(404);
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
              });
          path(
              ApiPath.TRANSACTIONS,
              () -> get("/:transactionId", transactionController.readById(), responseTransformer));
        });
  }
}

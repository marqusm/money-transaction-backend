package com.marqusm.example.moneytransaction.libimpl.vertx.rest;

import com.marqusm.example.moneytransaction.common.constant.ApiPath;
import com.marqusm.example.moneytransaction.libimpl.vertx.controller.AccountController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 06-Feb-20
 */
@Slf4j
@AllArgsConstructor
public class VertxRestVerticle extends AbstractVerticle {

  private final AccountController accountController;

  @Override
  public void start(Promise<Void> promise) {
    Promise<HttpServer> future = Promise.promise();
    vertx
        .createHttpServer()
        .requestHandler(createRouter(vertx))
        .listen(config().getInteger("http.port", 4567), future);
    promise
        .future()
        .onSuccess(
            response -> {
              log.info("VertxRestVerticle started");
              promise.complete();
            })
        .onFailure(
            cause -> {
              log.error("VertxRestVerticle failed", cause);
              promise.fail(cause);
            });
  }

  private Router createRouter(Vertx vertx) {
    val router = Router.router(vertx);
    router.route(ApiPath.API_PREFIX + "/*").handler(BodyHandler.create());
    router
        .post(ApiPath.API_PREFIX + ApiPath.ACCOUNTS)
        .handler(accountController.createAccountHandler(vertx));
    router
        .get(ApiPath.API_PREFIX + ApiPath.ACCOUNTS + "/:accountId")
        .handler(accountController.findAccountByIdHandler(vertx));
    router
        .delete(ApiPath.API_PREFIX + ApiPath.ACCOUNTS + "/:accountId")
        .handler(accountController.deleteAccountByIdHandler(vertx));
    return router;
  }
}

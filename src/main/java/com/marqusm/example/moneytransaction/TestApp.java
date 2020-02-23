package com.marqusm.example.moneytransaction;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import lombok.val;

public class TestApp extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    val options = new DeploymentOptions();
    options.setInstances(8);
    vertx.deployVerticle(TestApp::new, options);
  }

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    HttpServer server =
        vertx.createHttpServer(
            //                new HttpServerOptions()
            //                        .setUseAlpn(true)
            //                        .setKeyCertOptions(new
            // JksOptions().setPath("io/vertx/example/java9/server-keystore.jks").setPassword("wibble"))
            //                        .setSsl(true)
            );
    server
        .requestHandler(
            req -> {
              req.response().end("Hello " + req.version());
            })
        .listen(8080);
  }
}

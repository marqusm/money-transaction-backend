package com.marqusm.example.moneytransaction.controller.base;

import static spark.Spark.*;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.marqusm.example.moneytransaction.configuration.ApiConfig;
import com.marqusm.example.moneytransaction.configuration.DefaultModule;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 28-Jan-20
 */
public abstract class ControllerITest {

  public static Injector createInjectorAndInitServer() {
    val injector = Guice.createInjector(new DefaultModule());
    val apiConfig = injector.getInstance(ApiConfig.class);
    apiConfig.establishApi();
    awaitInitialization();
    return injector;
  }

  public static void stopServer() {
    stop();
    awaitStop();
  }
}

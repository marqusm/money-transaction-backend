package com.marqusm.example.moneytransaction.spark.controller.base;

import static spark.Spark.*;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.marqusm.example.moneytransaction.spark.configuration.ApiConfig;
import com.marqusm.example.moneytransaction.spark.configuration.SparkJooqModule;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 28-Jan-20
 */
public abstract class ControllerITest {

  public static Injector createInjectorAndInitServer(Module module) {
    val injector = Guice.createInjector(module);
    val apiConfig = injector.getInstance(ApiConfig.class);
    apiConfig.establishApi();
    awaitInitialization();
    return injector;
  }

  public static Injector createInjectorAndInitServer() {
    return createInjectorAndInitServer(new SparkJooqModule());
  }

  public static void stopServer() {
    stop();
    awaitStop();
  }
}

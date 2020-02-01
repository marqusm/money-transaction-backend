package com.marqusm.example.moneytransaction.controller.base;

import static spark.Spark.*;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.marqusm.example.moneytransaction.libimpl.spark.StartupRunner;
import com.marqusm.example.moneytransaction.libimpl.spark.configuration.ApiConfig;
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
    return createInjectorAndInitServer(StartupRunner.generateModule());
  }

  public static void stopServer() {
    stop();
    awaitStop();
  }
}

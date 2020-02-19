package com.marqusm.example.moneytransaction.libimpl.spark.controller.base;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.marqusm.example.moneytransaction.common.rest.RestStarter;
import com.marqusm.example.moneytransaction.common.util.StartupRunner;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 28-Jan-20
 */
public abstract class ControllerITest {
  private static RestStarter restStarter;

  public static Injector createInjectorAndInitServer(Module module) {
    val injector = Guice.createInjector(module);
    restStarter = injector.getInstance(RestStarter.class);
    restStarter.startRest();
    return injector;
  }

  public static Injector createInjectorAndInitServer() {
    return createInjectorAndInitServer(StartupRunner.generateModule());
  }

  public static void stopServer() {
    restStarter.stopRest();
  }
}

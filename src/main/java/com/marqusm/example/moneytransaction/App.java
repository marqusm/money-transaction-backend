package com.marqusm.example.moneytransaction;

import com.google.inject.Guice;
import com.marqusm.example.moneytransaction.common.rest.RestStarter;
import com.marqusm.example.moneytransaction.common.util.StartupRunner;
import java.io.IOException;
import java.util.Properties;
import lombok.val;
import org.flywaydb.core.Flyway;

public class App {

  public static void main(String[] args) throws IOException {
    val injector = Guice.createInjector(StartupRunner.generateModule());
    val restStarter = injector.getInstance(RestStarter.class);
    val flywayProperties = new Properties();
    flywayProperties.load(App.class.getResourceAsStream("/flyway.properties"));
    Flyway.configure().configuration(flywayProperties).load().migrate();
    restStarter.startRest();
  }
}

package com.marqusm.example.moneytransaction;

import com.google.inject.Guice;
import com.marqusm.example.moneytransaction.common.configuration.AppConfig;
import com.marqusm.example.moneytransaction.common.rest.RestStarter;
import com.marqusm.example.moneytransaction.common.util.StartupRunner;
import java.util.Properties;
import lombok.val;
import org.flywaydb.core.Flyway;

public class App {

  public static void main(String[] args) {
    val injector = Guice.createInjector(StartupRunner.generateModule());
    val restStarter = injector.getInstance(RestStarter.class);
    val flywayProperties = new Properties();
    val dbConfig = AppConfig.getLocalConfig().getMoneyTransactionService().getDatabase();
    flywayProperties.setProperty("flyway.url", dbConfig.getUrl());
    flywayProperties.setProperty("flyway.user", dbConfig.getUser());
    flywayProperties.setProperty("flyway.password", dbConfig.getPassword());
    Flyway.configure().configuration(flywayProperties).load().migrate();
    restStarter.startRest();
  }
}

package com.marqusm.example.moneytransaction;

import static spark.Spark.awaitInitialization;

import com.google.inject.Guice;
import com.marqusm.example.moneytransaction.libimpl.spark.StartupRunner;
import com.marqusm.example.moneytransaction.libimpl.spark.configuration.ApiConfig;
import java.io.IOException;
import java.util.Properties;
import lombok.val;
import org.flywaydb.core.Flyway;

public class App {

  public static void main(String[] args) throws IOException {
    val injector = Guice.createInjector(StartupRunner.generateModule());
    val apiConfig = injector.getInstance(ApiConfig.class);
    val flywayProperties = new Properties();
    flywayProperties.load(App.class.getResourceAsStream("/flyway.properties"));
    Flyway.configure().configuration(flywayProperties).load().migrate();
    apiConfig.establishApi();
    awaitInitialization();
  }
}

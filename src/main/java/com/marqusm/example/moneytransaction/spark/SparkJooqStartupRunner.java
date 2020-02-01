package com.marqusm.example.moneytransaction.spark;

import static spark.Spark.awaitInitialization;

import com.google.inject.Guice;
import com.marqusm.example.moneytransaction.App;
import com.marqusm.example.moneytransaction.spark.configuration.ApiConfig;
import com.marqusm.example.moneytransaction.spark.configuration.SparkJooqModule;
import java.io.IOException;
import java.util.Properties;
import lombok.val;
import org.flywaydb.core.Flyway;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
public class SparkJooqStartupRunner {
  public void run() throws IOException {
    val injector = Guice.createInjector(new SparkJooqModule());
    val apiConfig = injector.getInstance(ApiConfig.class);
    val flywayProperties = new Properties();
    flywayProperties.load(App.class.getResourceAsStream("/flyway.properties"));
    Flyway.configure().configuration(flywayProperties).load().migrate();
    apiConfig.establishApi();
    awaitInitialization();
  }
}

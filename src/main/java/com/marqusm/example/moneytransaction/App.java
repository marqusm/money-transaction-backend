package com.marqusm.example.moneytransaction;

import static spark.Spark.awaitInitialization;

import com.google.inject.Guice;
import com.marqusm.example.moneytransaction.configuration.ApiConfig;
import com.marqusm.example.moneytransaction.configuration.DefaultModule;
import lombok.val;

public class App {

  public static void main(String[] args) {
    val injector = Guice.createInjector(new DefaultModule());
    val apiConfig = injector.getInstance(ApiConfig.class);
    apiConfig.establishApi();
    awaitInitialization();
  }
}

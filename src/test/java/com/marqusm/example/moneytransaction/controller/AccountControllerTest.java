package com.marqusm.example.moneytransaction.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static spark.Spark.*;

import com.google.inject.Guice;
import com.marqusm.example.moneytransaction.TestData;
import com.marqusm.example.moneytransaction.configuration.ApiConfig;
import com.marqusm.example.moneytransaction.configuration.DefaultModule;
import com.marqusm.example.moneytransaction.model.Account;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
@Slf4j
class AccountControllerTest {

  @BeforeAll
  static void setUp() {
    log.info("Test class setup");
    val injector = Guice.createInjector(new DefaultModule());
    val apiConfig = injector.getInstance(ApiConfig.class);
    apiConfig.establishApi();
    awaitInitialization();
  }

  @AfterAll
  static void cleanUp() {
    log.info("Test class cleanup");
    stop();
    awaitStop();
  }

  @Test
  void create() {
    given()
        .body(new Account(null, null))
        .post(TestData.API_PREFIX + "/accounts")
        .andReturn()
        .peek()
        .then()
        .statusCode(200)
        .body("id", notNullValue(), "amount", equalTo(0f));
  }

  @Test
  void read() {
    val accountId =
        given()
            .body(new Account(null, null))
            .post(TestData.API_PREFIX + "/accounts")
            .andReturn()
            .as(Account.class)
            .getId();

    given()
        .body(new Account(null, null))
        .get(TestData.API_PREFIX + "/accounts/{accountId}", accountId)
        .andReturn()
        .then()
        .statusCode(200)
        .body("id", equalTo(accountId.toString()), "amount", equalTo(0f));
  }

  @Test
  void delete() {
    val accountId =
        given()
            .body(new Account(null, null))
            .post(TestData.API_PREFIX + "/accounts")
            .andReturn()
            .as(Account.class)
            .getId();

    given()
        .body(new Account(null, null))
        .get(TestData.API_PREFIX + "/accounts/{accountId}", accountId)
        .andReturn()
        .then()
        .statusCode(200)
        .body("id", equalTo(accountId.toString()), "amount", equalTo(0f));

    given()
        .delete(TestData.API_PREFIX + "/accounts/{accountId}", accountId)
        .andReturn()
        .then()
        .statusCode(200)
        .body(equalTo("null"));

    given()
        .body(new Account(null, null))
        .get(TestData.API_PREFIX + "/accounts/{accountId}", accountId)
        .andReturn()
        .then()
        .statusCode(404)
        .body("message", containsString("not found"));
  }
}

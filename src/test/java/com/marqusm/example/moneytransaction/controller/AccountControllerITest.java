package com.marqusm.example.moneytransaction.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import com.marqusm.example.moneytransaction.TestData;
import com.marqusm.example.moneytransaction.controller.base.ControllerITest;
import com.marqusm.example.moneytransaction.model.Account;
import io.restassured.http.ContentType;
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
class AccountControllerITest extends ControllerITest {

  @BeforeAll
  static void setUp() {
    createInjectorAndInitServer();
  }

  @AfterAll
  static void cleanUp() {
    stopServer();
  }

  @Test
  void create() {
    given()
        .contentType(ContentType.JSON.toString())
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
            .contentType(ContentType.JSON.toString())
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
            .contentType(ContentType.JSON.toString())
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

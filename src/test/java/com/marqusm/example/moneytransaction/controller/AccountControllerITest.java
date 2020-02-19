package com.marqusm.example.moneytransaction.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import com.marqusm.example.moneytransaction.TestData;
import com.marqusm.example.moneytransaction.controller.base.ControllerITest;
import com.marqusm.example.moneytransaction.model.Account;
import com.marqusm.example.moneytransaction.repository.AccountRepository;
import io.restassured.http.ContentType;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
@Slf4j
class AccountControllerITest extends ControllerITest {

  private static AccountRepository accountRepository;

  @BeforeAll
  static void setUp() {
    val injector = createInjectorAndInitServer();
    accountRepository = injector.getInstance(AccountRepository.class);
  }

  @AfterAll
  static void cleanUp() {
    stopServer();
  }

  @Test
  void create() {
    val account =
        given()
            .contentType(ContentType.JSON.toString())
            .body(new Account(null, null))
            .post(TestData.API_PREFIX + "/accounts")
            .andReturn()
            .peek()
            .then()
            .statusCode(200)
            .body("id", notNullValue(), "amount", equalTo(0f))
            .extract()
            .body()
            .as(Account.class);

    Assertions.assertNotNull(accountRepository.getById(account.getId()));
  }

  @Test
  void read_nonExisting() {
    given()
        .get(TestData.API_PREFIX + "/accounts/" + UUID.randomUUID())
        .andReturn()
        .then()
        .statusCode(404)
        .body("message", not(emptyString()));
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

    Assertions.assertNotNull(accountRepository.getById(accountId));
  }
}

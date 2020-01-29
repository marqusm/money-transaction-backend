package com.marqusm.example.moneytransaction.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import com.marqusm.example.moneytransaction.TestData;
import com.marqusm.example.moneytransaction.controller.base.ControllerITest;
import com.marqusm.example.moneytransaction.model.Account;
import com.marqusm.example.moneytransaction.model.Transaction;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.UUID;
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
class TransactionControllerITest extends ControllerITest {

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
    val transactionAmount = 100.f;

    val accountA =
        given()
            .contentType(ContentType.JSON.toString())
            .body(new Account(null, BigDecimal.valueOf(1_000)))
            .post(TestData.API_PREFIX + "/accounts")
            .andReturn()
            .as(Account.class);

    val accountB =
        given()
            .contentType(ContentType.JSON.toString())
            .body(new Account(null, BigDecimal.ZERO))
            .post(TestData.API_PREFIX + "/accounts")
            .andReturn()
            .as(Account.class);

    given()
        .contentType(ContentType.JSON.toString())
        .body(new Transaction(null, null, accountB.getId(), BigDecimal.valueOf(-transactionAmount)))
        .post(TestData.API_PREFIX + "/accounts/" + accountA.getId() + "/transactions")
        .andReturn()
        .then()
        .statusCode(200)
        .body("id", notNullValue());

    given()
        .get(TestData.API_PREFIX + "/accounts/" + accountB.getId())
        .andReturn()
        .then()
        .statusCode(200)
        .body("amount", equalTo(transactionAmount));

    given()
        .get(TestData.API_PREFIX + "/accounts/" + accountA.getId())
        .andReturn()
        .then()
        .statusCode(200)
        .body("amount", equalTo(accountA.getAmount().floatValue() - transactionAmount));
  }

  @Test
  void create_negativeBalance() {
    val accountA =
        given()
            .contentType(ContentType.JSON.toString())
            .body(new Account(null, BigDecimal.ZERO))
            .post(TestData.API_PREFIX + "/accounts")
            .andReturn()
            .as(Account.class);

    val accountB =
        given()
            .contentType(ContentType.JSON.toString())
            .body(new Account(null, BigDecimal.ZERO))
            .post(TestData.API_PREFIX + "/accounts")
            .andReturn()
            .as(Account.class);

    given()
        .contentType(ContentType.JSON.toString())
        .body(new Transaction(null, null, accountB.getId(), BigDecimal.valueOf(-100)))
        .post(TestData.API_PREFIX + "/accounts/" + accountA.getId() + "/transactions")
        .andReturn()
        .then()
        .statusCode(400)
        .body("message", not(emptyString()));

    given()
        .contentType(ContentType.JSON.toString())
        .body(new Transaction(null, null, accountB.getId(), BigDecimal.valueOf(100)))
        .post(TestData.API_PREFIX + "/accounts/" + accountA.getId() + "/transactions")
        .andReturn()
        .then()
        .statusCode(400)
        .body("message", not(emptyString()));
  }

  @Test
  void read() {
    val transactionAmount = 100.f;

    val accountA =
        given()
            .contentType(ContentType.JSON.toString())
            .body(new Account(null, BigDecimal.valueOf(1_000)))
            .post(TestData.API_PREFIX + "/accounts")
            .andReturn()
            .as(Account.class);

    val accountB =
        given()
            .contentType(ContentType.JSON.toString())
            .body(new Account(null, null))
            .post(TestData.API_PREFIX + "/accounts")
            .andReturn()
            .as(Account.class);

    val testTransaction =
        given()
            .contentType(ContentType.JSON.toString())
            .body(
                new Transaction(
                    null, null, accountB.getId(), BigDecimal.valueOf(-transactionAmount)))
            .post(TestData.API_PREFIX + "/accounts/" + accountA.getId() + "/transactions")
            .andReturn()
            .as(Transaction.class);

    given()
        .get(TestData.API_PREFIX + "/transactions/" + testTransaction.getId())
        .andReturn()
        .then()
        .statusCode(200)
        .body("amount", equalTo(-transactionAmount));
  }

  @Test
  void read_nonExisting() {
    given()
        .get(TestData.API_PREFIX + "/transactions/" + UUID.randomUUID())
        .andReturn()
        .then()
        .statusCode(404)
        .body("message", not(emptyString()));
  }
}

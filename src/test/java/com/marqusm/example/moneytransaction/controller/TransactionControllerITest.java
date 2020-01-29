package com.marqusm.example.moneytransaction.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import com.marqusm.example.moneytransaction.TestData;
import com.marqusm.example.moneytransaction.controller.base.ControllerITest;
import com.marqusm.example.moneytransaction.model.Account;
import com.marqusm.example.moneytransaction.model.Transaction;
import com.marqusm.example.moneytransaction.repository.AccountRepository;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

  private static UUID MASTER_ACCOUNT_ID;

  @BeforeAll
  static void setUp() {
    val injector = createInjectorAndInitServer();

    val accountRepository = injector.getInstance(AccountRepository.class);
    val account =
        new Account(
            UUID.randomUUID(), BigDecimal.valueOf(1_000_000).setScale(2, RoundingMode.HALF_UP));
    account.setMetaActive(Boolean.TRUE);
    accountRepository.save(account);
    MASTER_ACCOUNT_ID = account.getId();
  }

  @AfterAll
  static void cleanUp() {
    stopServer();
  }

  @Test
  void create() {
    val transactionAmount = 100.f;

    val masterAccount =
        given()
            .get(TestData.API_PREFIX + "/accounts/" + MASTER_ACCOUNT_ID)
            .andReturn()
            .as(Account.class);

    val testAccount =
        given()
            .contentType(ContentType.JSON.toString())
            .body(new Account(null, null))
            .post(TestData.API_PREFIX + "/accounts")
            .andReturn()
            .as(Account.class);

    given()
        .contentType(ContentType.JSON.toString())
        .body(
            new Transaction(
                null, null, testAccount.getId(), BigDecimal.valueOf(-transactionAmount)))
        .post(TestData.API_PREFIX + "/accounts/" + MASTER_ACCOUNT_ID + "/transactions")
        .andReturn()
        .then()
        .statusCode(200)
        .body("id", notNullValue());

    given()
        .get(TestData.API_PREFIX + "/accounts/" + testAccount.getId())
        .andReturn()
        .then()
        .statusCode(200)
        .body("amount", equalTo(transactionAmount));

    given()
        .get(TestData.API_PREFIX + "/accounts/" + MASTER_ACCOUNT_ID)
        .andReturn()
        .then()
        .statusCode(200)
        .body("amount", equalTo(masterAccount.getAmount().floatValue() - transactionAmount));
  }

  @Test
  void read() {
    val transactionAmount = 100.f;

    val testAccount =
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
                    null, null, testAccount.getId(), BigDecimal.valueOf(-transactionAmount)))
            .post(TestData.API_PREFIX + "/accounts/" + MASTER_ACCOUNT_ID + "/transactions")
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
  void createWithNoFunds() {
    val transactionAmount = 1.f;

    val testAccount =
        given()
            .contentType(ContentType.JSON.toString())
            .body(new Account(null, null))
            .post(TestData.API_PREFIX + "/accounts")
            .andReturn()
            .as(Account.class);

    given()
        .contentType(ContentType.JSON.toString())
        .body(
            new Transaction(null, null, MASTER_ACCOUNT_ID, BigDecimal.valueOf(-transactionAmount)))
        .post(TestData.API_PREFIX + "/accounts/" + testAccount.getId() + "/transactions")
        .andReturn()
        .then()
        .statusCode(400)
        .body("message", not(emptyString()));
  }
}

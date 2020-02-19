package com.marqusm.example.moneytransaction.libimpl.spark.controller;

import static io.restassured.RestAssured.given;

import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.marqusm.example.moneytransaction.TestData;
import com.marqusm.example.moneytransaction.common.model.dto.Account;
import com.marqusm.example.moneytransaction.common.model.dto.Transaction;
import com.marqusm.example.moneytransaction.common.util.StartupRunner;
import com.marqusm.example.moneytransaction.configuration.TransactionConcurrencyModule;
import com.marqusm.example.moneytransaction.libimpl.spark.controller.base.ControllerITest;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author : Marko
 * @createdOn : 29-Jan-20
 */
@Slf4j
class TransactionControllerCTest extends ControllerITest {

  private static final int THREAD_COUNT = 2;
  private static final int TRANSACTIONS_COUNT = 5;

  @Disabled
  @Test
  void createSlowTransactions() {
    val totalTime =
        createTransactions(
            Modules.override(StartupRunner.generateModule())
                .with(new TransactionConcurrencyModule()));
    log.info("Total time: " + totalTime);
    Assertions.assertTrue(totalTime > 2400);
  }

  @Test
  void createNormalTransactions() {
    val totalTime = createTransactions(StartupRunner.generateModule());
    log.info("Total time: " + totalTime);
    Assertions.assertTrue(totalTime < 1000);
  }

  private long createTransactions(Module module) {
    createInjectorAndInitServer(module);

    val accountA =
        given()
            .contentType(ContentType.JSON)
            .body(new Account(null, BigDecimal.valueOf(1_000)))
            .post(TestData.API_PREFIX + "/accounts")
            .andReturn()
            .as(Account.class);
    val accountB =
        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .post(TestData.API_PREFIX + "/accounts")
            .andReturn()
            .as(Account.class);

    val remainingCount = new AtomicInteger(TRANSACTIONS_COUNT);
    ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
    for (int i = 0; i < 3; i++) {
      executor.execute(
          () -> {
            while (remainingCount.get() > 0) {
              remainingCount.decrementAndGet();
              given()
                  .contentType(ContentType.JSON)
                  .body(new Transaction(null, null, accountB.getId(), BigDecimal.valueOf(-1.f)))
                  .post(TestData.API_PREFIX + "/accounts/" + accountA.getId() + "/transactions")
                  .andReturn()
                  .then()
                  .statusCode(200);
            }
          });
    }

    val startTime = LocalDateTime.now();
    executor.shutdown();
    try {
      if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException ex) {
      log.error("Executor shutdown failed", ex);
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }

    val totalTime =
        LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
            - startTime.toInstant(ZoneOffset.UTC).toEpochMilli();

    stopServer();

    return totalTime;
  }
}

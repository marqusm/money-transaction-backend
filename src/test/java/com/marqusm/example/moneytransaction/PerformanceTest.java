package com.marqusm.example.moneytransaction;

import static io.restassured.RestAssured.given;
import static spark.Spark.*;

import com.google.inject.Guice;
import com.marqusm.example.moneytransaction.configuration.ApiConfig;
import com.marqusm.example.moneytransaction.configuration.DefaultModule;
import com.marqusm.example.moneytransaction.model.dto.Account;
import com.marqusm.example.moneytransaction.model.dto.Transaction;
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

/**
 * @author : Marko
 * @createdOn : 29-Jan-20
 */
@Slf4j
public class PerformanceTest {

  private static final String URL_PREFIX = "http://localhost:4567/api/v1";

  public static void main(String[] args) {
    AtomicInteger REQUESTS_COUNT = new AtomicInteger(10);
    val THREAD_COUNT = 2;

    val injector = Guice.createInjector(new DefaultModule());
    val apiConfig = injector.getInstance(ApiConfig.class);
    apiConfig.establishApi();
    awaitInitialization();

    val accountA =
        given()
            .contentType(ContentType.JSON)
            .body("{}")
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

    val startTime = LocalDateTime.now();
    val remainingCount = new AtomicInteger(REQUESTS_COUNT.get());
    ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
    for (int i = 0; i < THREAD_COUNT; i++) {
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
    executor.shutdown();
    try {
      if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
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
    stop();
    awaitStop();
    log.info(
        "Requests ratio: "
            + ((double) REQUESTS_COUNT.get() / ((double) totalTime / 1000))
            + " req/s");
  }
}

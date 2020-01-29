package com.marqusm.example.moneytransaction.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

import com.marqusm.example.moneytransaction.TestData;
import com.marqusm.example.moneytransaction.controller.base.ControllerITest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author : Marko
 * @createdOn : 28-Jan-20
 */
public class IllegalRequestControllerITest extends ControllerITest {
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
        .contentType(ContentType.TEXT)
        .body("Test body content")
        .post(TestData.API_PREFIX + "/accounts")
        .andReturn()
        .then()
        .statusCode(406)
        .body("message", not(emptyString()));
  }
}

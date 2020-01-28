package com.marqusm.example.moneytransaction;

import static spark.Spark.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AppTest {
  @Test
  void appStartupTest() {
    App.main(new String[0]);
    Assertions.assertTrue(activeThreadCount() > 0);
    stop();
    awaitStop();
  }
}

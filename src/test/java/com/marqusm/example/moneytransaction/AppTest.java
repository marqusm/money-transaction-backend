package com.marqusm.example.moneytransaction;

import static spark.Spark.*;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AppTest {
  @Test
  void appStartupTest() throws IOException {
    App.main(new String[0]);
    awaitInitialization();
    Assertions.assertTrue(activeThreadCount() > 0);
    stop();
    awaitStop();
  }
}

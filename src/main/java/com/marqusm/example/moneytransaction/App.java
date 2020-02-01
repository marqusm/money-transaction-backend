package com.marqusm.example.moneytransaction;

import com.marqusm.example.moneytransaction.spark.SparkJooqStartupRunner;
import java.io.IOException;

public class App {

  public static void main(String[] args) throws IOException {
    new SparkJooqStartupRunner().run();
  }
}

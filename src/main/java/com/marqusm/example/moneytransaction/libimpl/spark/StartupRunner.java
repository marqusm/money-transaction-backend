package com.marqusm.example.moneytransaction.libimpl.spark;

import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.marqusm.example.moneytransaction.common.configuration.depinjection.DefaultModule;
import com.marqusm.example.moneytransaction.common.configuration.depinjection.JooqDatabaseModule;
import com.marqusm.example.moneytransaction.common.configuration.depinjection.SparkApiModule;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
public class StartupRunner {
  public void run() {}

  public static Module generateModule() {
    return Modules.combine(new JooqDatabaseModule(), new SparkApiModule(), new DefaultModule());
  }
}

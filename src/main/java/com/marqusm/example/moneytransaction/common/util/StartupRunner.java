package com.marqusm.example.moneytransaction.common.util;

import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.marqusm.example.moneytransaction.common.configuration.AppConfig;
import com.marqusm.example.moneytransaction.common.configuration.depinjection.*;
import com.marqusm.example.moneytransaction.common.constant.RepositoryImplementation;
import com.marqusm.example.moneytransaction.common.constant.RestImplementation;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
public class StartupRunner {
  public void run() {}

  public static Module generateModule() {
    return Modules.combine(
        new DefaultModule(),
        getModule(AppConfig.getRepositoryImplementation()),
        getModule(AppConfig.getRestImplementation()));
  }

  private static Module getModule(RestImplementation restImplementation) {
    switch (restImplementation) {
      case SPARK:
        return new SparkRestModule();
      case VERTX:
        return new VertxRestModule();
      default:
        throw new IllegalArgumentException("Argument not valid: " + restImplementation);
    }
  }

  private static Module getModule(RepositoryImplementation repositoryImplementation) {
    switch (repositoryImplementation) {
      case IN_MEMORY:
        return new InMemoryDatabaseModule();
      case JOOQ:
        return new JooqDatabaseModule();
      case VERTX_PG:
        return new VertxPgDatabaseModule();
      default:
        throw new IllegalArgumentException("Argument not valid: " + repositoryImplementation);
    }
  }
}

package com.marqusm.example.moneytransaction.libimpl.spark;

import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.marqusm.example.moneytransaction.common.configuration.ApplicationConfiguration;
import com.marqusm.example.moneytransaction.common.configuration.depinjection.DefaultModule;
import com.marqusm.example.moneytransaction.common.configuration.depinjection.InMemoryDatabaseModule;
import com.marqusm.example.moneytransaction.common.configuration.depinjection.JooqDatabaseModule;
import com.marqusm.example.moneytransaction.common.configuration.depinjection.SparkApiModule;
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
        getModule(ApplicationConfiguration.getRepositoryImplementation()),
        getModule(ApplicationConfiguration.getRestImplementation()));
  }

  private static Module getModule(RestImplementation restImplementation) {
    return new SparkApiModule();
  }

  private static Module getModule(RepositoryImplementation repositoryImplementation) {
    switch (repositoryImplementation) {
      case IN_MEMORY:
        return new InMemoryDatabaseModule();
      case JOOQ:
        return new JooqDatabaseModule();
      default:
        throw new IllegalArgumentException("Argument not valid: " + repositoryImplementation);
    }
  }
}

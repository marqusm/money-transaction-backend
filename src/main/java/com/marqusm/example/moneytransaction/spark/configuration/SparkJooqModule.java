package com.marqusm.example.moneytransaction.spark.configuration;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.marqusm.example.moneytransaction.spark.controller.AccountController;
import com.marqusm.example.moneytransaction.spark.controller.TransactionController;
import com.marqusm.example.moneytransaction.spark.repository.AccountRepository;
import com.marqusm.example.moneytransaction.spark.repository.TransactionRepository;
import com.marqusm.example.moneytransaction.spark.service.AccountService;
import com.marqusm.example.moneytransaction.spark.service.TransactionService;
import com.marqusm.example.moneytransaction.spark.transformer.JsonGsonTransformer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.NoArgsConstructor;
import lombok.val;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import spark.ResponseTransformer;

/**
 * @author : Marko
 * @createdOn : 25-Jan-20
 */
@NoArgsConstructor
public class SparkJooqModule implements Module {
  public void configure(Binder binder) {
    binder.bind(ResponseTransformer.class).to(JsonGsonTransformer.class);

    binder.bind(AccountRepository.class).in(Singleton.class);
    binder.bind(TransactionRepository.class).in(Singleton.class);
    binder.bind(AccountService.class).in(Singleton.class);
    binder.bind(TransactionService.class).in(Singleton.class);
    binder.bind(AccountController.class).in(Singleton.class);
    binder.bind(TransactionController.class).in(Singleton.class);
  }

  @Provides
  DSLContext provideSqlContext(DataSource dataSource) {
    val configuration = new DefaultConfiguration().set(SQLDialect.POSTGRES).set(dataSource);
    return DSL.using(configuration);
  }

  @Provides
  DataSource provideDataSource() {
    val config = new HikariConfig();
    config.setJdbcUrl("jdbc:postgresql://localhost:5435/money-transactions");
    config.setUsername("postgres");
    config.setPassword("postgres");
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    return new HikariDataSource(config);
  }
}

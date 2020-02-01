package com.marqusm.example.moneytransaction.common.configuration.depinjection;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.marqusm.example.moneytransaction.common.repository.AccountRepository;
import com.marqusm.example.moneytransaction.common.repository.TransactionRepository;
import com.marqusm.example.moneytransaction.libimpl.jooq.repository.JooqAccountRepositoryImpl;
import com.marqusm.example.moneytransaction.libimpl.jooq.repository.JooqTransactionRepositoryImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.NoArgsConstructor;
import lombok.val;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
@NoArgsConstructor
public class JooqDatabaseModule implements Module {
  @Override
  public void configure(Binder binder) {}

  @Provides
  @Singleton
  AccountRepository provideAccountRepository(DSLContext dslContext) {
    return new JooqAccountRepositoryImpl(dslContext);
  }

  @Provides
  @Singleton
  TransactionRepository provideTransactionRepository(DSLContext dslContext) {
    return new JooqTransactionRepositoryImpl(dslContext);
  }

  @Provides
  @Singleton
  DSLContext provideDslContext(DataSource dataSource) {
    val configuration = new DefaultConfiguration().set(SQLDialect.POSTGRES).set(dataSource);
    return DSL.using(configuration);
  }

  @Provides
  @Singleton
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

package com.marqusm.example.moneytransaction.common.configuration.depinjection;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.marqusm.example.moneytransaction.common.configuration.AppConfig;
import com.marqusm.example.moneytransaction.common.model.dto.Account;
import com.marqusm.example.moneytransaction.common.model.dto.Transaction;
import com.marqusm.example.moneytransaction.common.model.dto.TransactionCollection;
import com.marqusm.example.moneytransaction.common.repository.AccountRepository;
import com.marqusm.example.moneytransaction.common.repository.TransactionRepository;
import com.marqusm.example.moneytransaction.libimpl.vertxpg.repository.VertxAccountRepository;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import java.util.Optional;
import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.val;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.impl.DSL;

/**
 * @author : Marko
 * @createdOn : 05-Feb-20
 */
@NoArgsConstructor
public class VertxPgDatabaseModule implements Module {
  @Override
  public void configure(Binder binder) {}

  @Provides
  @Singleton
  AccountRepository provideAccountRepository() {
    return new AccountRepository() {
      @Override
      public Account save(Account account) {
        return null;
      }

      @Override
      public Optional<Account> findById(UUID id) {
        return Optional.empty();
      }

      @Override
      public void inactivate(UUID id) {}
    };
  }

  @Provides
  @Singleton
  VertxAccountRepository provideAccountRepository(PgPool client, DSLContext dslContext) {
    return new VertxAccountRepository(client, dslContext);
  }

  //  @Provides
  //  @Singleton
  //  TransactionRepository provideTransactionRepository(PgPool client) {
  //    return new VertxPgTransactionRepositoryImpl(client);
  //  }

  @Provides
  @Singleton
  TransactionRepository provideTransactionRepository() {
    return new TransactionRepository() {
      @Override
      public Optional<Transaction> findById(UUID id) {
        return Optional.empty();
      }

      @Override
      public TransactionCollection findByAccountId(UUID accountId) {
        return null;
      }

      @Override
      public Transaction transferMoney(Transaction transaction) {
        return null;
      }
    };
  }

  @Provides
  @Singleton
  PgConnectOptions providePgConnectOptions() {
    val databaseConfig = AppConfig.getLocalConfig().getMoneyTransactionService().getDatabase();
    return new PgConnectOptions()
        .setHost(databaseConfig.getHost())
        .setPort(databaseConfig.getPort())
        .setDatabase(databaseConfig.getDatabase())
        .setUser(databaseConfig.getUser())
        .setPassword(databaseConfig.getPassword());
  }

  @Provides
  @Singleton
  PoolOptions providePoolOptions() {
    val databaseConfig = AppConfig.getLocalConfig().getMoneyTransactionService().getDatabase();
    return new PoolOptions().setMaxSize(databaseConfig.getMaxConnections());
  }

  @Provides
  @Singleton
  PgPool providePoolOptions(PgConnectOptions connectOptions, PoolOptions poolOptions) {
    return PgPool.pool(connectOptions, poolOptions);
  }

  @Provides
  @Singleton
  DSLContext provideDSLContext() {
    return DSL.using(
        SQLDialect.POSTGRES, new Settings().withStatementType(StatementType.STATIC_STATEMENT));
  }
}

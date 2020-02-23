package com.marqusm.example.moneytransaction.libimpl.vertxpg.repository;

import static com.marqusm.example.moneytransaction.common.model.generated.jooq.Tables.ACCOUNT;

import com.marqusm.example.moneytransaction.common.constant.SqlQuery;
import com.marqusm.example.moneytransaction.common.model.dto.Account;
import com.marqusm.example.moneytransaction.libimpl.vertx.util.AsyncQueryExecutor;
import io.vertx.core.Promise;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Tuple;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 05-Feb-20
 */
@Slf4j
@RequiredArgsConstructor
public class VertxAccountRepository {
  private final PgPool pgPool;

  public void save(Account account, Promise<Account> promise) {
    val queryExecutor = new AsyncQueryExecutor<Account>(pgPool, promise);
    val timestamp = Timestamp.from(OffsetDateTime.now().toInstant());
    queryExecutor.run(
        SqlQuery.CREATE_ACCOUNT,
        Tuple.of(account.getBalance(), true, timestamp, timestamp),
        rowSet -> {
          val row = rowSet.iterator().next();
          return new Account(
              row.getUUID(ACCOUNT.ACCOUNT_ID.getName()),
              row.getBigDecimal(ACCOUNT.BALANCE.getName()));
        });
  }

  public void findById(UUID accountId, Promise<Optional<Account>> promise) {
    val queryExecutor = new AsyncQueryExecutor<Optional<Account>>(pgPool, promise);
    queryExecutor.run(
        SqlQuery.READ_ACCOUNT_BY_ID,
        Tuple.of(accountId),
        rowSet -> {
          if (rowSet.size() == 0) {
            return Optional.empty();
          } else {
            val row = rowSet.iterator().next();
            return Optional.of(
                new Account(
                    row.getUUID(ACCOUNT.ACCOUNT_ID.getName()),
                    row.getBigDecimal(ACCOUNT.BALANCE.getName())));
          }
        });
  }

  public void inactivate(UUID accountId, Promise<Void> promise) {
    val queryExecutor = new AsyncQueryExecutor<Void>(pgPool, promise);
    queryExecutor.run(SqlQuery.INACTIVATE_ACCOUNT_BY_ID, Tuple.of(accountId), rowSet -> null);
  }
}

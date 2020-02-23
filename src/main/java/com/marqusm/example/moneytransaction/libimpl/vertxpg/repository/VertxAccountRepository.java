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
import org.jooq.DSLContext;

/**
 * @author : Marko
 * @createdOn : 05-Feb-20
 */
@Slf4j
@RequiredArgsConstructor
public class VertxAccountRepository {
  private final PgPool pgPool;
  private final DSLContext create;

  public void save(Account account, Promise<Account> promise) {
    val queryExecutor = new AsyncQueryExecutor<Account>(pgPool, promise);
    val timestamp = Timestamp.from(OffsetDateTime.now().toInstant());
    queryExecutor.run(
        //        create
        //            .insertInto(
        //                ACCOUNT,
        //                ACCOUNT.BALANCE,
        //                ACCOUNT.META_ACTIVE,
        //                ACCOUNT.META_CREATION_DATE,
        //                ACCOUNT.META_MODIFIED_DATE)
        //            .values(account.getBalance(), Boolean.TRUE, timestamp, timestamp)
        //            .returning()
        //            .getSQL(),
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
        create
            .select()
            .from(ACCOUNT)
            .where(ACCOUNT.META_ACTIVE.eq(Boolean.TRUE).and(ACCOUNT.ACCOUNT_ID.eq(accountId)))
            .getSQL(),
        //        SqlQuery.READ_ACCOUNT_BY_ID,
        //        Tuple.of(accountId),
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
    queryExecutor.run(
        create
            .update(ACCOUNT)
            .set(ACCOUNT.META_ACTIVE, Boolean.FALSE)
            .where(ACCOUNT.ACCOUNT_ID.eq(accountId))
            .getSQL(),
        rowSet -> null);
  }
}

package com.marqusm.example.moneytransaction.libimpl.vertx.util;

import io.vertx.core.Promise;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 09-Feb-20
 */
@Slf4j
@AllArgsConstructor
public class AsyncQueryExecutor<R> {
  private final PgPool pgPool;
  private final Promise<R> responsePromise;

  public void run(String query, Function<RowSet<Row>, R> processResponse) {
    final Promise<SqlConnection> connPromise = Promise.promise();
    pgPool.getConnection(connPromise);
    connPromise
        .future()
        .onSuccess(
            conn -> {
              final Promise<RowSet<Row>> queryPromise = Promise.promise();
              conn.query(query, queryPromise);
              queryPromise
                  .future()
                  .onSuccess(
                      rowSet -> {
                        val response = processResponse.apply(rowSet);
                        conn.close();
                        responsePromise.complete(response);
                      })
                  .onFailure(
                      cause -> {
                        onDatabaseActionFailed(query, conn, cause, responsePromise);
                      });
            })
        .onFailure(cause -> onDatabaseConnectionFailed(cause, responsePromise));
  }

  public void run(String preparedQuery, Tuple tuple, Function<RowSet<Row>, R> processResponse) {
    final Promise<SqlConnection> connPromise = Promise.promise();
    pgPool.getConnection(connPromise);
    connPromise
        .future()
        .onSuccess(
            conn -> {
              final Promise<RowSet<Row>> queryPromise = Promise.promise();
              conn.preparedQuery(preparedQuery, tuple, queryPromise);
              queryPromise
                  .future()
                  .onSuccess(
                      rowSet -> {
                        val response = processResponse.apply(rowSet);
                        conn.close();
                        responsePromise.complete(response);
                      })
                  .onFailure(
                      cause -> {
                        onDatabaseActionFailed(preparedQuery, conn, cause, responsePromise);
                      });
            })
        .onFailure(cause -> onDatabaseConnectionFailed(cause, responsePromise));
  }

  private void onDatabaseConnectionFailed(Throwable cause, Promise<R> promise) {
    log.error("Database connection failed.", cause);
    promise.fail(cause);
  }

  private void onDatabaseActionFailed(
      String query, SqlConnection conn, Throwable cause, Promise<R> promise) {
    log.error("Database action failed: " + query, cause);
    conn.close();
    promise.fail(cause);
  }
}

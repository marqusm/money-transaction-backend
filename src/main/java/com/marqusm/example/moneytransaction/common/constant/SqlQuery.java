package com.marqusm.example.moneytransaction.common.constant;

/**
 * @author : Marko
 * @createdOn : 09-Feb-20
 */
public class SqlQuery {
  public static final String CREATE_ACCOUNT =
      "INSERT INTO account (\"balance\", \"meta_active\", \"meta_creation_date\", \"meta_modified_date\") VALUES ($1, $2, CAST($3 AS TIMESTAMP), CAST($4 AS TIMESTAMP)) RETURNING *";
  public static final String READ_ACCOUNT_BY_ID =
      "SELECT * FROM account WHERE meta_active = true AND account_id = CAST($1 AS UUID)";
}

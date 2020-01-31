package com.marqusm.example.moneytransaction.repository;

import static com.marqusm.example.moneytransaction.model.database.Tables.TRANSACTION;
import static com.marqusm.example.moneytransaction.model.database.tables.Account.ACCOUNT;

import com.google.inject.Inject;
import com.marqusm.example.moneytransaction.exception.BadRequestException;
import com.marqusm.example.moneytransaction.model.dto.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public class TransactionRepository {
  private final DSLContext create;

  public Transaction getById(UUID id) {
    val record =
        create
            .selectFrom(TRANSACTION)
            .where(TRANSACTION.TRANSACTION_ID.eq(id), TRANSACTION.META_ACTIVE.eq(Boolean.TRUE))
            .fetchOptional()
            .orElseThrow();
    return new Transaction(
        record.getTransactionId(),
        record.getAccountId(),
        record.getRelatedAccountId(),
        record.getAmount());
  }

  public Transaction transferMoney(Transaction transaction) {
    val timestamp = Timestamp.from(OffsetDateTime.now().toInstant());
    val result = new AtomicReference<Transaction>();
    create.transaction(
        configuration -> {
          val accountA =
              DSL.using(configuration)
                  .selectFrom(ACCOUNT)
                  .where(
                      ACCOUNT.ACCOUNT_ID.eq(transaction.getAccountId()),
                      ACCOUNT.META_ACTIVE.eq(Boolean.TRUE))
                  .fetchOptional()
                  .orElseThrow();
          val accountB =
              DSL.using(configuration)
                  .selectFrom(ACCOUNT)
                  .where(
                      ACCOUNT.ACCOUNT_ID.eq(transaction.getRelatedAccountId()),
                      ACCOUNT.META_ACTIVE.eq(Boolean.TRUE))
                  .fetchOptional()
                  .orElseThrow();

          if (accountA.getBalance().add(transaction.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException(
                "Transaction would make negative balance: " + transaction);
          }
          if (accountB.getBalance().subtract(transaction.getAmount()).compareTo(BigDecimal.ZERO)
              < 0) {
            throw new BadRequestException(
                "Transaction would make negative balance: " + transaction);
          }

          accountA.setBalance(
              accountA.getBalance().add(transaction.getAmount().setScale(2, RoundingMode.HALF_UP)));
          accountA.setMetaModifiedDate(timestamp);
          accountA.store();

          accountB.setBalance(
              accountB
                  .getBalance()
                  .subtract(transaction.getAmount().setScale(2, RoundingMode.HALF_UP)));
          accountB.setMetaModifiedDate(timestamp);
          accountB.store();

          val transactionRecord = DSL.using(configuration).newRecord(TRANSACTION);
          transactionRecord.setAccountId(transaction.getAccountId());
          transactionRecord.setRelatedAccountId(transaction.getRelatedAccountId());
          transactionRecord.setAmount(transaction.getAmount());
          transactionRecord.setTimestamp(timestamp);
          transactionRecord.setMetaActive(Boolean.TRUE);
          transactionRecord.setMetaCreationDate(timestamp);
          transactionRecord.setMetaModifiedDate(timestamp);
          transactionRecord.store();

          result.set(
              new Transaction(
                  transactionRecord.getTransactionId(),
                  transactionRecord.getAccountId(),
                  transactionRecord.getRelatedAccountId(),
                  transactionRecord.getAmount()));
        });
    return result.get();
  }
}

package com.marqusm.example.moneytransaction.repository;

import static com.marqusm.example.moneytransaction.model.database.tables.Account.ACCOUNT;

import com.google.inject.Inject;
import com.marqusm.example.moneytransaction.model.dto.Account;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;
import org.jooq.DSLContext;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public class AccountRepository {
  private final DSLContext create;

  public Account save(Account account) {
    val timestamp = Timestamp.from(OffsetDateTime.now().toInstant());
    val record = create.newRecord(ACCOUNT);
    record.setBalance(account.getBalance());
    record.setMetaActive(Boolean.TRUE);
    record.setMetaCreationDate(timestamp);
    record.setMetaModifiedDate(timestamp);
    record.store();
    return new Account(record.getAccountId(), record.getBalance());
  }

  public Account getById(UUID id) {
    val record =
        create
            .selectFrom(ACCOUNT)
            .where(ACCOUNT.ACCOUNT_ID.eq(id), ACCOUNT.META_ACTIVE.eq(Boolean.TRUE))
            .fetchOptional()
            .orElseThrow();
    return new Account(record.getAccountId(), record.getBalance());
  }

  public void inactivate(UUID id) {
    val record = create.selectFrom(ACCOUNT).where(ACCOUNT.ACCOUNT_ID.eq(id)).fetchOne();
    record.setMetaActive(Boolean.FALSE);
    record.store();
  }
}

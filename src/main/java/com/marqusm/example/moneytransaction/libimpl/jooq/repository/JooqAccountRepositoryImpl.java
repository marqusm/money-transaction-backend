package com.marqusm.example.moneytransaction.libimpl.jooq.repository;

import static com.marqusm.example.moneytransaction.common.model.generated.jooq.Tables.ACCOUNT;

import com.marqusm.example.moneytransaction.common.model.dto.Account;
import com.marqusm.example.moneytransaction.common.repository.AccountRepository;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.val;
import org.jooq.DSLContext;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
@AllArgsConstructor
public class JooqAccountRepositoryImpl implements AccountRepository {
  private final DSLContext create;

  @Override
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

  @Override
  public Optional<Account> findById(UUID id) {
    return create
        .selectFrom(ACCOUNT)
        .where(ACCOUNT.ACCOUNT_ID.eq(id), ACCOUNT.META_ACTIVE.eq(Boolean.TRUE))
        .fetchOptional()
        .map(r -> new Account(r.getAccountId(), r.getBalance()));
  }

  @Override
  public void inactivate(UUID id) {
    val record = create.selectFrom(ACCOUNT).where(ACCOUNT.ACCOUNT_ID.eq(id)).fetchOne();
    record.setMetaActive(Boolean.FALSE);
    record.store();
  }
}

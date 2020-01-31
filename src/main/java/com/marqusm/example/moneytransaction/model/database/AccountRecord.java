package com.marqusm.example.moneytransaction.model.database;

import com.marqusm.example.moneytransaction.model.database.base.AbstractRecord;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : Marko
 * @createdOn : 30-Jan-20
 */
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountRecord extends AbstractRecord {
  private final UUID accountId;
  private BigDecimal balance;
}

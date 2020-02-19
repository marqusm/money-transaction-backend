package com.marqusm.example.moneytransaction.model;

import com.marqusm.example.moneytransaction.model.base.AbstractModel;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

/**
 * @author : Marko
 * @createdOn : 25-Jan-20
 */
@AllArgsConstructor
@Data
@With
@EqualsAndHashCode(callSuper = false)
public class Transaction extends AbstractModel {
  private final UUID id;
  private final UUID accountId;
  private final UUID relatedAccountId;
  private final BigDecimal amount;
}

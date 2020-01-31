package com.marqusm.example.moneytransaction.model.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : Marko
 * @createdOn : 25-Jan-20
 */
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class Transaction {
  private final UUID id;
  private final UUID accountId;
  private final UUID relatedAccountId;
  private final BigDecimal amount;
}

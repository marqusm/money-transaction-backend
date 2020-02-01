package com.marqusm.example.moneytransaction.common.model.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : Marko
 * @createdOn : 25-Jan-20
 */
@AllArgsConstructor
@Data
public class Transaction {
  private UUID id;
  private UUID accountId;
  private UUID relatedAccountId;
  private BigDecimal amount;
}

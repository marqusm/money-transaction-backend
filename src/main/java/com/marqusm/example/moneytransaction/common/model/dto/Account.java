package com.marqusm.example.moneytransaction.common.model.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Marko
 * @createdOn : 25-Jan-20
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {
  private UUID id;
  private BigDecimal balance;

  public void addAmount(BigDecimal amount) {
    this.balance = this.balance.add(amount);
  }

  public void subtractAmount(BigDecimal amount) {
    this.balance = this.balance.subtract(amount);
  }
}

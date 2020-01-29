package com.marqusm.example.moneytransaction.model;

import com.marqusm.example.moneytransaction.model.base.AbstractModel;
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
@EqualsAndHashCode(callSuper = false)
public class Account extends AbstractModel {
  private final UUID id;
  private BigDecimal amount;

  public void addAmount(BigDecimal amount) {
    this.amount = this.amount.add(amount);
  }

  public void subtractAmount(BigDecimal amount) {
    this.amount = this.amount.subtract(amount);
  }
}

package com.marqusm.example.moneytransaction.common.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class TransactionCollection {
  private final List<Transaction> collection;
  private final int count;
}

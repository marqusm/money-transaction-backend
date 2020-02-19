package com.marqusm.example.moneytransaction.common.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionCollection {
  private List<Transaction> collection;
  private long count;
}

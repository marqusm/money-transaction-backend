package com.marqusm.example.moneytransaction.common.service;

import com.marqusm.example.moneytransaction.common.model.dto.Transaction;
import com.marqusm.example.moneytransaction.common.model.dto.TransactionCollection;
import java.util.UUID;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
public interface TransactionService {
  Transaction createTransaction(Transaction transaction);

  Transaction getTransaction(UUID transactionId);

  TransactionCollection getTransactionByAccountId(UUID accountId);
}

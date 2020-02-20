package com.marqusm.example.moneytransaction.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.marqusm.example.moneytransaction.TestData;
import com.marqusm.example.moneytransaction.exception.ConflictException;
import lombok.val;
import org.junit.jupiter.api.Test;

/**
 * @author : Marko
 * @createdOn : 20-Feb-20
 */
class TransactionRepositoryTest {

  @Test
  void save() {
    val transaction = TestData.createTransaction();
    val transactionRepository = new TransactionRepository();

    transactionRepository.save(transaction);
    assertThrows(ConflictException.class, () -> transactionRepository.save(transaction));
  }
}

package com.marqusm.example.moneytransaction.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.marqusm.example.moneytransaction.TestData;
import com.marqusm.example.moneytransaction.model.Transaction;
import com.marqusm.example.moneytransaction.repository.AccountRepository;
import com.marqusm.example.moneytransaction.repository.TransactionRepository;
import java.util.UUID;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author : Marko
 * @createdOn : 19-Feb-20
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

  @InjectMocks private TransactionService transactionService;
  @Mock private AccountRepository accountRepository;
  @Mock private TransactionRepository transactionRepository;

  @AfterEach
  public void cleanUp() {
    Mockito.reset(accountRepository);
    Mockito.reset(transactionRepository);
  }

  @Test
  void createTransaction() {
    val testAccount1 = TestData.createAccount().withId(UUID.randomUUID());
    val testAccount2 = TestData.createAccount().withId(UUID.randomUUID());
    val testTransaction =
        TestData.createTransaction()
            .withAccountId(testAccount1.getId())
            .withRelatedAccountId(testAccount2.getId());
    when(accountRepository.getById(testAccount1.getId())).thenReturn(testAccount1);
    when(accountRepository.getById(testAccount2.getId())).thenReturn(testAccount2);
    when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

    val transaction = transactionService.createTransaction(testTransaction);

    Assertions.assertEquals(testTransaction, transaction);
  }

  @Test
  void getTransaction() {
    val testTransaction = TestData.createTransaction();
    when(transactionRepository.getById(testTransaction.getId())).thenReturn(testTransaction);

    val transaction = transactionService.getTransaction(testTransaction.getId());

    Assertions.assertEquals(testTransaction, transaction);
  }
}

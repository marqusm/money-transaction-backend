package com.marqusm.example.moneytransaction.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.marqusm.example.moneytransaction.TestData;
import com.marqusm.example.moneytransaction.model.Account;
import com.marqusm.example.moneytransaction.repository.AccountRepository;
import java.math.BigDecimal;
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
class AccountServiceTest {

  @InjectMocks private AccountService accountService;
  @Mock private AccountRepository accountRepository;

  @AfterEach
  public void cleanUp() {
    Mockito.reset(accountRepository);
  }

  @Test
  void createAccount() {
    val testAccount = TestData.createAccount();
    when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

    val account = new Account(null, BigDecimal.ONE);
    val createdAccount = accountService.createAccount(account);

    Assertions.assertNotNull(createdAccount);
    Assertions.assertEquals(testAccount, createdAccount);
  }

  @Test
  void getAccount() {
    val testAccount = TestData.createAccount();
    when(accountRepository.getById(testAccount.getId())).thenReturn(testAccount);

    val fetchedAccount = accountService.getAccount(testAccount.getId());

    Assertions.assertNotNull(fetchedAccount);
    Assertions.assertEquals(testAccount, fetchedAccount);
  }

  @Test
  void deleteAccount() {
    val testAccount = TestData.createAccount();
    when(accountRepository.getById(testAccount.getId())).thenReturn(testAccount);

    accountService.deleteAccount(testAccount.getId());
  }
}

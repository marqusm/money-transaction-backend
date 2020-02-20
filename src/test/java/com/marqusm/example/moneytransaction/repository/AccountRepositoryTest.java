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
class AccountRepositoryTest {

  @Test
  void save() {
    val account = TestData.createAccount();
    val accountRepository = new AccountRepository();

    accountRepository.save(account);
    assertThrows(ConflictException.class, () -> accountRepository.save(account));
  }
}

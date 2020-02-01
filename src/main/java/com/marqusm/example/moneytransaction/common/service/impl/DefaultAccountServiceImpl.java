package com.marqusm.example.moneytransaction.common.service.impl;

import com.marqusm.example.moneytransaction.common.exception.NotFoundException;
import com.marqusm.example.moneytransaction.common.model.dto.Account;
import com.marqusm.example.moneytransaction.common.repository.AccountRepository;
import com.marqusm.example.moneytransaction.common.service.AccountService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 25-Jan-20
 */
@AllArgsConstructor
public class DefaultAccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;

  public Account createAccount(Account accountRequest) {
    val account =
        new Account(
            UUID.randomUUID(),
            Optional.ofNullable(accountRequest.getBalance())
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP));
    return accountRepository.save(account);
  }

  public Account getAccount(UUID accountId) {
    return accountRepository
        .findById(accountId)
        .orElseThrow(() -> new NotFoundException("Account not found: " + accountId));
  }

  public Void deleteAccount(UUID accountId) {
    accountRepository.inactivate(accountId);
    return null;
  }
}

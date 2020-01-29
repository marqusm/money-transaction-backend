package com.marqusm.example.moneytransaction.service;

import com.google.inject.Inject;
import com.marqusm.example.moneytransaction.model.Account;
import com.marqusm.example.moneytransaction.repository.AccountRepository;
import com.marqusm.example.moneytransaction.util.RepositoryUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 25-Jan-20
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public class AccountService {

  private final AccountRepository accountRepository;

  public Account createAccount(Account accountRequest) {
    val account =
        new Account(
            UUID.randomUUID(),
            Optional.ofNullable(accountRequest.getAmount())
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP));
    account.setMetaActive(Boolean.TRUE);
    return accountRepository.save(account);
  }

  public Account getAccount(UUID accountId) {
    val account = accountRepository.getById(accountId);
    RepositoryUtil.checkIfExists(account, "Account not found: " + accountId);
    return account;
  }

  public void deleteAccount(UUID accountId) {
    val account = accountRepository.getById(accountId);
    RepositoryUtil.checkIfExists(account, "Account not found: " + accountId);
    account.setMetaActive(Boolean.FALSE);
    accountRepository.update(account);
  }
}

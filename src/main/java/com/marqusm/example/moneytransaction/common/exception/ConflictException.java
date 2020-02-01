package com.marqusm.example.moneytransaction.common.exception;

import com.marqusm.example.moneytransaction.common.exception.base.ClientHttpException;

/**
 * @author : Marko
 * @createdOn : 28-Jan-20
 */
public class ConflictException extends ClientHttpException {
  public ConflictException(String message) {
    super(409, message, null);
  }
}

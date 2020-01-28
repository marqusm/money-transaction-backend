package com.marqusm.example.moneytransaction.exception;

import com.marqusm.example.moneytransaction.exception.base.ClientHttpException;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
public class ConflictException extends ClientHttpException {
  public ConflictException(String message) {
    super(409, message, null);
  }
}

package com.marqusm.example.moneytransaction.exception;

import com.marqusm.example.moneytransaction.exception.base.ClientHttpException;

/**
 * @author : Marko
 * @createdOn : 28-Jan-20
 */
public class BadRequestException extends ClientHttpException {
  public BadRequestException(String message) {
    super(400, message, null);
  }
}

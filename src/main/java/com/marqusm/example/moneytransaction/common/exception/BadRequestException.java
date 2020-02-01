package com.marqusm.example.moneytransaction.common.exception;

import com.marqusm.example.moneytransaction.common.exception.base.ClientHttpException;

/**
 * @author : Marko
 * @createdOn : 28-Jan-20
 */
public class BadRequestException extends ClientHttpException {
  public BadRequestException(String message) {
    super(400, message, null);
  }
}

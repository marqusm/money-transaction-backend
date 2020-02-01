package com.marqusm.example.moneytransaction.common.exception;

import com.marqusm.example.moneytransaction.common.exception.base.ClientHttpException;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
public class NotFoundException extends ClientHttpException {
  public NotFoundException(String message) {
    super(404, message, null);
  }
}

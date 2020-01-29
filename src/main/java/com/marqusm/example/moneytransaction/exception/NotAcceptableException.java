package com.marqusm.example.moneytransaction.exception;

import com.marqusm.example.moneytransaction.exception.base.ClientHttpException;

/**
 * @author : Marko
 * @createdOn : 28-Jan-20
 */
public class NotAcceptableException extends ClientHttpException {
  public NotAcceptableException(String message) {
    super(406, message, null);
  }
}

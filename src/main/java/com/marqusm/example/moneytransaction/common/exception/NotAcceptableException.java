package com.marqusm.example.moneytransaction.common.exception;

import com.marqusm.example.moneytransaction.common.exception.base.ClientHttpException;

/**
 * @author : Marko
 * @createdOn : 28-Jan-20
 */
public class NotAcceptableException extends ClientHttpException {
  public NotAcceptableException(String message) {
    super(406, message, null);
  }
}

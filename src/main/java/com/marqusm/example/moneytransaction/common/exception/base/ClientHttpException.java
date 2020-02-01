package com.marqusm.example.moneytransaction.common.exception.base;

import lombok.Getter;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
@Getter
public class ClientHttpException extends RuntimeException {

  private final int statusCode;

  public ClientHttpException(int statusCode, String message, Throwable throwable) {
    super(message, throwable);
    this.statusCode = statusCode;
  }
}

package com.marqusm.example.moneytransaction.exception;

/**
 * @author : Marko
 * @createdOn : 28-Jan-20
 */
public class NotAcceptableException extends RuntimeException {
  public NotAcceptableException(String message) {
    super(message, null);
  }
}

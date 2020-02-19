package com.marqusm.example.moneytransaction.exception;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    super(message, null);
  }
}

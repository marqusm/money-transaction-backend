package com.marqusm.example.moneytransaction.exception;

/**
 * @author : Marko
 * @createdOn : 28-Jan-20
 */
public class ConflictException extends RuntimeException {
  public ConflictException(String message) {
    super(message, null);
  }
}

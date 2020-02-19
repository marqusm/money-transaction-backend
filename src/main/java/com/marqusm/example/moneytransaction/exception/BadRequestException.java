package com.marqusm.example.moneytransaction.exception;

/**
 * @author : Marko
 * @createdOn : 28-Jan-20
 */
public class BadRequestException extends RuntimeException {
  public BadRequestException(String message) {
    super(message, null);
  }
}

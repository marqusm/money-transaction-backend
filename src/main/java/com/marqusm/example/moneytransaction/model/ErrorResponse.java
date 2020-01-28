package com.marqusm.example.moneytransaction.model;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
@AllArgsConstructor
@Value
public class ErrorResponse {
  private String message;
}

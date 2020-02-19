package com.marqusm.example.moneytransaction.common.model.dto;

import lombok.*;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponse {
  private String message;

  public ErrorResponse(Throwable cause) {
    this.message = cause.getMessage();
  }
}

package com.marqusm.example.moneytransaction.util;

import com.marqusm.example.moneytransaction.exception.NotFoundException;
import com.marqusm.example.moneytransaction.model.database.base.AbstractRecord;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
public class RepositoryUtil {
  public static void checkIfExists(AbstractRecord model, String errorMessage) {
    if (model == null || model.getMetaActive().equals(Boolean.FALSE)) {
      throw new NotFoundException(errorMessage);
    }
  }
}

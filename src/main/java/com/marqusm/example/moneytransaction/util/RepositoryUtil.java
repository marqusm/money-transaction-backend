package com.marqusm.example.moneytransaction.util;

import com.marqusm.example.moneytransaction.exception.NotFoundException;
import com.marqusm.example.moneytransaction.model.base.AbstractModel;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
public class RepositoryUtil {
  public static void checkIfExists(AbstractModel model, String errorMessage) {
    if (model == null || model.getMetaActive().equals(Boolean.FALSE)) {
      throw new NotFoundException(errorMessage);
    }
  }
}

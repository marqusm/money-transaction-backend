package com.marqusm.example.moneytransaction.filter;

import com.marqusm.example.moneytransaction.constant.ContentTypeName;
import com.marqusm.example.moneytransaction.exception.NotAcceptableException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import spark.Request;

/**
 * @author : Marko
 * @createdOn : 28-Jan-20
 */
public class ContentTypeFilter {
  private static final Set<String> METHOD_TO_CHECK_SET =
      new HashSet<>(Arrays.asList("POST", "PUT", "PATCH"));

  public void apply(Request request) {
    if (METHOD_TO_CHECK_SET.contains(request.requestMethod())
        && !(request.contentType().equals(ContentTypeName.APPLICATION_JSON)
            || request
                .contentType()
                .equals(ContentTypeName.APPLICATION_JSON + "; charset=UTF-8"))) {
      throw new NotAcceptableException("Content Type is not acceptable: " + request.contentType());
    }
  }
}

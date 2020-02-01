package com.marqusm.example.moneytransaction.libimpl.spark.transformer;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import spark.ResponseTransformer;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
@AllArgsConstructor
public class JsonGsonTransformer implements ResponseTransformer {

  private final Gson gson;

  @Override
  public String render(Object model) {
    return gson.toJson(model);
  }
}

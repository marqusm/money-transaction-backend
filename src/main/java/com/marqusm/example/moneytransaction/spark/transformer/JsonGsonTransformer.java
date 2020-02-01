package com.marqusm.example.moneytransaction.spark.transformer;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.marqusm.example.moneytransaction.common.annotation.JsonExclude;
import spark.ResponseTransformer;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
public class JsonGsonTransformer implements ResponseTransformer {

  private final Gson gson;

  public JsonGsonTransformer() {
    ExclusionStrategy strategy =
        new ExclusionStrategy() {
          @Override
          public boolean shouldSkipField(FieldAttributes field) {
            return field.getAnnotation(JsonExclude.class) != null;
          }

          @Override
          public boolean shouldSkipClass(Class<?> clazz) {
            return false;
          }
        };
    gson = new GsonBuilder().addSerializationExclusionStrategy(strategy).create();
  }

  @Override
  public String render(Object model) {
    return gson.toJson(model);
  }
}

package com.marqusm.example.moneytransaction.libimpl.vertx.validator;

import com.google.gson.Gson;
import com.marqusm.example.moneytransaction.common.model.dto.ErrorResponse;
import com.marqusm.example.moneytransaction.libimpl.vertx.util.RoutingContextUtil;
import io.vertx.ext.web.RoutingContext;
import lombok.AllArgsConstructor;

/**
 * @author : Marko
 * @createdOn : 09-Feb-20
 */
@AllArgsConstructor
public class Validator {

  private final Gson gson;
  private final RoutingContextUtil routingContextUtil;

  public <T> void validate(String json, Class<T> klass, RoutingContext routingContext) {
    try {
      gson.fromJson(json, klass);
    } catch (Exception e) {
      routingContextUtil.fillResponse(routingContext, 400, new ErrorResponse("Illegal request"));
    }
  }
}

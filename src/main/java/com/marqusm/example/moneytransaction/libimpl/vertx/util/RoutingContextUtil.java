package com.marqusm.example.moneytransaction.libimpl.vertx.util;

import com.google.gson.Gson;
import com.marqusm.example.moneytransaction.common.constant.HttpHeaderName;
import com.marqusm.example.moneytransaction.common.model.dto.ErrorResponse;
import com.marqusm.example.moneytransaction.libimpl.vertx.constant.MessageHeader;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.web.RoutingContext;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 09-Feb-20
 */
@Slf4j
@AllArgsConstructor
public class RoutingContextUtil {
  private final Gson gson;

  public void fillSuccessfulResponse(RoutingContext request, String body) {
    request
        .response()
        .putHeader(HttpHeaderName.X_Request_ID, UUID.randomUUID().toString())
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(body);
  }

  public void fillSuccessfulResponse(RoutingContext request, Message<String> message) {
    val errorHeader = message.headers().get(MessageHeader.TYPE);
    if (MessageHeader.Type.ERROR.equals(errorHeader)) {
      val status = Integer.parseInt(message.headers().get(MessageHeader.STATUS));
      request
          .response()
          .putHeader(HttpHeaderName.X_Request_ID, UUID.randomUUID().toString())
          .putHeader("content-type", "application/json")
          .setStatusCode(status)
          .end(gson.toJson(new ErrorResponse(message.body())));
    } else {
      request
          .response()
          .putHeader(HttpHeaderName.X_Request_ID, UUID.randomUUID().toString())
          .putHeader("content-type", "application/json")
          .setStatusCode(200);
      if (message.body() != null) {
        request.response().end(message.body());
      } else {
        request.response().end();
      }
    }
  }

  public void fillResponse(RoutingContext request, int status, Object body) {
    request
        .response()
        .putHeader(HttpHeaderName.X_Request_ID, UUID.randomUUID().toString())
        .putHeader("content-type", "application/json")
        .setStatusCode(status);
    if (body != null) {
      request.response().end(gson.toJson(body));
    } else {
      request.response().end();
    }
  }

  public void onFailure(RoutingContext request, String message, Throwable cause) {
    log.error(message, cause);
    request
        .response()
        .putHeader(HttpHeaderName.X_Request_ID, UUID.randomUUID().toString())
        .putHeader("content-type", "application/json")
        .setStatusCode(500)
        .end(gson.toJson(new ErrorResponse(cause)));
    request.fail(cause);
  }
}

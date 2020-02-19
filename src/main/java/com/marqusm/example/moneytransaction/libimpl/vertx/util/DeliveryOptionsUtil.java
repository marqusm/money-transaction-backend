package com.marqusm.example.moneytransaction.libimpl.vertx.util;

import com.marqusm.example.moneytransaction.libimpl.vertx.constant.MessageHeader;
import io.vertx.core.eventbus.DeliveryOptions;

/**
 * @author : Marko
 * @createdOn : 09-Feb-20
 */
public class DeliveryOptionsUtil {
  public static DeliveryOptions createErrorDeliveryOptions(int status) {
    return new DeliveryOptions()
        .addHeader(MessageHeader.TYPE, MessageHeader.Type.ERROR)
        .addHeader(MessageHeader.STATUS, status + "");
  }
}

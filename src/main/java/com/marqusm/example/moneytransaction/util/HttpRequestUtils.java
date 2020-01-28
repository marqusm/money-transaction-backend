package com.marqusm.example.moneytransaction.util;

import com.marqusm.example.moneytransaction.constant.HttpHeaderName;
import java.util.Date;
import spark.Request;
import spark.Response;

/**
 * @author : Marko
 * @createdOn : 27-Jan-20
 */
public class HttpRequestUtils {
  public static String toPrettyString(Request request, String requestId) {
    return "Received API call:"
        + "\n\tID: "
        + requestId
        + "\n\tUrl: "
        + request.url()
        + "\n\tMethod: "
        + request.requestMethod()
        + "\n\tProtocol: "
        + request.protocol()
        + "\n\tDate: "
        + new Date().toString()
        + "\n\tContent-Type: "
        + request.contentType()
        + "\n\tBody: "
        + request.body();
  }

  public static String toPrettyString(Response response) {
    return "Response sent:"
        //        + "\nUrl: "
        //        + response.url()
        //        + "\nMethod: "
        //        + response.requestMethod()
        //        + "\nProtocol: "
        //        + response.protocol()
        + "\n\tID: "
        + response.raw().getHeader(HttpHeaderName.X_Request_ID)
        + "\n\tDate: "
        + new Date().toString()
        + "\n\tStatus: "
        + response.status()
        + "\n\tBody: "
        + response.body();
  }

  public static String toPrettyExceptionString(Request request, Response response) {
    return "Exception occurred while processing API call:"
        + "\n\tID: "
        + response.raw().getHeader(HttpHeaderName.X_Request_ID)
        + "\n\tUrl: "
        + request.url()
        + "\n\tMethod: "
        + request.requestMethod()
        + "\n\tProtocol: "
        + request.protocol()
        + "\n\tDate: "
        + new Date().toString()
        + "\n\tContent-Type: "
        + request.contentType()
        + "\n\tBody: "
        + request.body();
  }
}

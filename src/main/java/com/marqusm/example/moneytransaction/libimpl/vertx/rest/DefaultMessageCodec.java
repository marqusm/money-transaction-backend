package com.marqusm.example.moneytransaction.libimpl.vertx.rest;

import com.google.gson.Gson;
import com.marqusm.example.moneytransaction.common.model.dto.Account;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import lombok.AllArgsConstructor;
import lombok.val;

/**
 * @author : Marko
 * @createdOn : 08-Feb-20
 */
@AllArgsConstructor
public class DefaultMessageCodec implements MessageCodec<Account, Account> {

  private final Gson gson;

  @Override
  public void encodeToWire(Buffer buffer, Account account) {
    val json = gson.toJson(account);
    buffer.appendInt(json.getBytes().length).appendString(json);
  }

  @Override
  public Account decodeFromWire(int pos, Buffer buffer) {
    val length = buffer.getInt(pos);
    val json = buffer.getString(pos + 4, pos + length + 4);
    return gson.fromJson(json, Account.class);
  }

  @Override
  public Account transform(Account account) {
    return account;
  }

  @Override
  public String name() {
    return this.getClass().getName();
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }
}

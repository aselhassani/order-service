package com.orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

  INVALID_ORDER_QTY(1001),
  ORDER_NOT_FOUND(1002),
  UPDATE_NOT_ALLOWED(1003);

  private final int value;

}

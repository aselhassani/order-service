package com.orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderNotFoundException extends RuntimeException {
  private final String orderNumber;
  @Override
  public String getMessage() {
    return String.format("No order with number '%s' was found", orderNumber);
  }
}

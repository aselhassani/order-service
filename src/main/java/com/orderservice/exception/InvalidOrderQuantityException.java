package com.orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidOrderQuantityException extends RuntimeException {
  private final Integer invalidQty;
  @Override
  public String getMessage(){
    return String.format("Invalid order qty: %d. Should be one of 5, 10, 15", invalidQty);
  }

}

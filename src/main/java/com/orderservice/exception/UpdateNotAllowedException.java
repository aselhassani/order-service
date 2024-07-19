package com.orderservice.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateNotAllowedException extends RuntimeException {

  @Override
  public String getMessage() {
    return "Update not allowed more than 5 minutes after the creation";
  }
}

package com.orderservice.controller;

import com.orderservice.dto.ErrorDTO;
import com.orderservice.exception.ErrorCode;
import com.orderservice.exception.InvalidOrderQuantityException;
import com.orderservice.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(value = InvalidOrderQuantityException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDTO handleInvalidOrderQuantityException(InvalidOrderQuantityException ex) {
    return new ErrorDTO(ErrorCode.INVALID_ORDER_QTY.getValue(), ex.getMessage());
  }

  @ExceptionHandler(value = OrderNotFoundException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorDTO handleOrderNotFoundException(OrderNotFoundException ex) {
    return new ErrorDTO(ErrorCode.ORDER_NOT_FOUND.getValue(), ex.getMessage());
  }

}

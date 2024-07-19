package com.orderservice.controller;

import com.orderservice.dto.CreateOrderRequestDTO;
import com.orderservice.dto.OrderResponseDTO;
import com.orderservice.dto.UpdateOrderRequestDTO;
import com.orderservice.exception.InvalidOrderQuantityException;
import com.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
@Tag(name = "Orders", description = "Orders API")
public class OrderController {
  private final OrderService orderService;
  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public OrderResponseDTO createOrder(@RequestBody @Valid CreateOrderRequestDTO dto) {

    if (!isOrderValidQuantity(dto.quantity())) {
      throw new InvalidOrderQuantityException(dto.quantity());
    }

    return orderService.createOrder(dto);
  }

  @PutMapping("/{orderNumber}")
  @ResponseStatus(HttpStatus.OK)
  public OrderResponseDTO updateOrder(@PathVariable(name = "orderNumber") String orderNumber, @RequestBody @Valid UpdateOrderRequestDTO dto) {

    if (!isOrderValidQuantity(dto.quantity())) {
      throw new InvalidOrderQuantityException(dto.quantity());
    }

    return orderService.updateOrder(orderNumber, dto);
  }

  public boolean isOrderValidQuantity(int quantity) {
    return List.of(5, 10, 15).contains(quantity);
  }
}

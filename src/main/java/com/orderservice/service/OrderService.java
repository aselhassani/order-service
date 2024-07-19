package com.orderservice.service;

import com.orderservice.config.AppConfiguration;
import com.orderservice.dto.CreateOrderRequestDTO;
import com.orderservice.dto.OrderResponseDTO;
import com.orderservice.dto.UpdateOrderRequestDTO;
import com.orderservice.exception.OrderNotFoundException;
import com.orderservice.model.Customer;
import com.orderservice.model.Order;
import com.orderservice.repository.CustomerRepository;
import com.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final AppConfiguration appConfig;
  private final TimeService timeService;
  private final OrderNumberGenerator orderNumberGenerator;

  public OrderResponseDTO createOrder(CreateOrderRequestDTO dto) {

    var unitPrice = appConfig.getUnitPrice();

    var customer = customerRepository.findByPhoneNumber(dto.phoneNumber())
        .orElseGet(() -> customerRepository.save(
            Customer.builder()
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .phoneNumber(dto.phoneNumber())
                .build())
        );

    var createdAt = timeService.now();
    var orderNumber = orderNumberGenerator.generateOrderNumber();
    var quantity = dto.quantity();
    var totalPrice = unitPrice * quantity;

    var order = Order.builder()
        .createdAt(createdAt)
        .updatedAt(createdAt)
        .deliveryAddress(dto.deliveryAddress())
        .customer(customer)
        .quantity(dto.quantity())
        .totalPrice(totalPrice)
        .number(orderNumber)
        .build();

    return OrderResponseDTO.fromEntity(orderRepository.save(order));
  }


  public OrderResponseDTO updateOrder(String orderNumber, UpdateOrderRequestDTO dto) {

    return orderRepository.findByNumber(orderNumber)
        .map(entity -> {
          var unitPrice = appConfig.getUnitPrice();
          var updatedAt = timeService.now();
          var quantity = dto.quantity();
          var deliveryAddress = dto.deliveryAddress();
          var totalPrice = quantity * unitPrice;

          return entity.withQuantity(quantity)
              .withUpdatedAt(updatedAt)
              .withDeliveryAddress(deliveryAddress)
              .withTotalPrice(totalPrice);
        })
        .map(orderRepository::save)
        .map(OrderResponseDTO::fromEntity)
        .orElseThrow(() -> new OrderNotFoundException(orderNumber));
  }
}

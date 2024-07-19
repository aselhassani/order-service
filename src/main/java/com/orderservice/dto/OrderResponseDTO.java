package com.orderservice.dto;

import com.orderservice.model.Order;
import java.time.Instant;
import lombok.Builder;

@Builder
public record OrderResponseDTO(
    String firstName,
    String lastName,
    String phoneNumber,
    Integer quantity,
    String orderNumber,
    String deliveryAddress,
    Instant createdAt,
    Instant updatedAt,
    Double totalPrice
) {

  public static OrderResponseDTO fromEntity(Order entity) {
    return OrderResponseDTO.builder()
        .firstName(entity.getCustomer().getFirstname())
        .lastName(entity.getCustomer().getLastName())
        .phoneNumber(entity.getCustomer().getPhoneNumber())
        .quantity(entity.getQuantity())
        .orderNumber(entity.getOrderNumber())
        .deliveryAddress(entity.getDeliveryAddress())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .totalPrice(entity.getTotalPrice())
        .build();
  }

}

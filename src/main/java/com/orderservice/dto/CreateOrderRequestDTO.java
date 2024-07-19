package com.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateOrderRequestDTO(
    @NotBlank
    String firstname,
    @NotBlank
    String lastname,
    @NotBlank
    String phoneNumber,
    @NotNull
    Integer quantity,
    @NotBlank
    String deliveryAddress
) {
}

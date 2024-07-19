package com.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateOrderRequestDTO(
    @NotNull
    Integer quantity,
    @NotBlank
    String deliveryAddress
) {

}

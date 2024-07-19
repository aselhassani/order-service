package com.orderservice.dto;

import lombok.Builder;

@Builder
public record ErrorDTO(Integer code, String message) {

}

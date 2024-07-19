package com.orderservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.orderservice.exception.InvalidOrderQuantityException;
import com.orderservice.service.OrderService;
import com.orderservice.test.TestHelper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
  @Mock
  private OrderService orderService;
  @InjectMocks
  private OrderController underTest;

  @ParameterizedTest
  @ValueSource(ints = {5, 10, 15})
  void createOrderShouldCreateOrderAndReturnIt(int quantity) {

    var createOrderRequestDTO = TestHelper.createCreateOrderRequestDTO(quantity);
    var orderResponseDTO = TestHelper.createOrderResponseDTO();

    when(orderService.createOrder(any())).thenReturn(orderResponseDTO);

    var result = underTest.createOrder(createOrderRequestDTO);

    verify(orderService).createOrder(createOrderRequestDTO);
    assertThat(result).isEqualTo(orderResponseDTO);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 6, 7, 8, 9, 11, 12, 13, 14, 16, 17, 18, 19, 20})
  void createOrderShouldThrowExceptionForInvalidOrderQuantity(int qty) {

    var dto = TestHelper.createCreateOrderRequestDTO(qty);

    assertThatExceptionOfType(InvalidOrderQuantityException.class)
        .isThrownBy(() -> underTest.createOrder(dto));

    verify(orderService, never()).createOrder(any());

  }

  @ParameterizedTest
  @ValueSource(ints = {5, 10, 15})
  void updateOrderShouldUpdateOrderAndReturnIt(int quantity) {

    var updateOrderRequestDTO = TestHelper.createUpdateOrderRequestDTO(quantity);
    var orderNumber = "XJ565B";
    var orderResponseDTO = TestHelper.createOrderResponseDTO();

    when(orderService.updateOrder(any(), any())).thenReturn(orderResponseDTO);

    var result = underTest.updateOrder(orderNumber, updateOrderRequestDTO);

    verify(orderService).updateOrder(orderNumber, updateOrderRequestDTO);
    assertThat(result).isEqualTo(orderResponseDTO);
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, -2, 3, 4, 6, 7, 8, 9, 11, 12, 13, 14, 16, 17, 18, 19, 20})
  void updateOrderShouldThrowExceptionForInvalidOrderQuantity(int qty) {

    var orderNumber = "CPJ567";
    var dto = TestHelper.createUpdateOrderRequestDTO(qty);

    assertThatExceptionOfType(InvalidOrderQuantityException.class)
        .isThrownBy(() -> underTest.updateOrder(orderNumber, dto));

    verify(orderService, never()).createOrder(any());

  }



}

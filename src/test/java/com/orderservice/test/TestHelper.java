package com.orderservice.test;


import com.orderservice.dto.CreateOrderRequestDTO;
import com.orderservice.dto.OrderResponseDTO;
import com.orderservice.dto.UpdateOrderRequestDTO;
import com.orderservice.model.Customer;
import com.orderservice.model.Order;
import java.time.Instant;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestHelper {
  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final String DIGITS = "0123456789";
  private static final Random random = new Random();

  public static String generateRandomString() {
    return generateRandomString(8);
  }

  public static String generateRandomString(int length) {
    var random = new Random();
    return IntStream.range(0, length)
        .mapToObj(i -> String.valueOf(CHARACTERS.charAt(random.nextInt(CHARACTERS.length()))))
        .collect(Collectors.joining());
  }

  public static String generateRandomPhoneNumber() {
    var random = new Random();
    return IntStream.range(0, 10)
        .mapToObj(i -> String.valueOf(DIGITS.charAt(random.nextInt(DIGITS.length()))))
        .collect(Collectors.joining());
  }

  public static CreateOrderRequestDTO createCreateOrderRequestDTO(int quantity) {

    return CreateOrderRequestDTO.builder()
        .firstName(generateRandomString())
        .lastName(generateRandomString())
        .quantity(quantity)
        .phoneNumber(generateRandomPhoneNumber())
        .deliveryAddress(generateRandomString(20))
        .build();
  }

  public static UpdateOrderRequestDTO createUpdateOrderRequestDTO(int quantity) {

    return UpdateOrderRequestDTO.builder()
        .quantity(quantity)
        .deliveryAddress(generateRandomString(20))
        .build();
  }

  public static CreateOrderRequestDTO createCreateOrderRequestDTO() {
    return createCreateOrderRequestDTO(10);
  }

  public static UpdateOrderRequestDTO createUpdateOrderRequestDTO() {
    return createUpdateOrderRequestDTO(10);
  }

  private static int generateValidQty() {
    var possibleQuantities = new int[] {5, 10, 15};
    return possibleQuantities[random.nextInt(0, 2)];
  }

  public static OrderResponseDTO createOrderResponseDTO() {

    Instant timestamp = Instant.now();



    return OrderResponseDTO.builder()
        .firstName(generateRandomString())
        .lastName(generateRandomString())
        .quantity(generateValidQty())
        .phoneNumber(generateRandomPhoneNumber())
        .deliveryAddress(generateRandomString(30))
        .orderNumber(generateRandomString(6))
        .createdAt(timestamp)
        .updatedAt(timestamp)
        .totalPrice(13.30)
        .build();
  }


  public static Customer createCustomer() {
    return Customer.builder()
        .id(random.nextLong())
        .firstname(generateRandomString())
        .lastName(generateRandomString())
        .phoneNumber(generateRandomPhoneNumber())
        .build();
  }

  public static Order createOrder() {
    return createOrder(Instant.now());
  }
  public static Order createOrder(Instant createdAt) {

    var unitPrice = random.nextDouble();
    var qty = generateValidQty();

    return Order.builder()
        .id(random.nextLong())
        .orderNumber(generateRandomString(6))
        .customer(createCustomer())
        .quantity(qty)
        .totalPrice(qty * unitPrice)
        .deliveryAddress(generateRandomString(30))
        .createdAt(createdAt)
        .updatedAt(createdAt)
        .build();
  }

  public static String generateRandomOrderNumber() {
    return generateRandomString(6);
  }
}

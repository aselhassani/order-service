package com.orderservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.orderservice.config.AppConfiguration;
import com.orderservice.dto.CreateOrderRequestDTO;
import com.orderservice.dto.OrderResponseDTO;
import com.orderservice.dto.UpdateOrderRequestDTO;
import com.orderservice.exception.OrderNotFoundException;
import com.orderservice.model.Customer;
import com.orderservice.model.Order;
import com.orderservice.repository.CustomerRepository;
import com.orderservice.repository.OrderRepository;
import com.orderservice.test.TestHelper;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;
  @Mock
  private CustomerRepository customerRepository;
  @Mock
  private AppConfiguration appConfiguration;
  @Mock
  private OrderNumberGenerator orderNumberGenerator;
  @Mock
  private TimeService timeService;
  @InjectMocks
  private OrderService underTest;

  private String orderNumber;
  private double unitPrice;
  private Instant timestamp;

  @BeforeEach
  void setup() {
    orderNumber = TestHelper.generateRandomString(6);
    unitPrice = new Random().nextDouble();
    timestamp = Instant.now();
  }


  @Test
  void createOrderShouldCreateOrderAndLinkItToExistingCustomer() {

    var customer = TestHelper.createCustomer();
    var createOrderRequestDTO = TestHelper.createCreateOrderRequestDTO();
    var persistedOrder = TestHelper.createOrder();


    when(appConfiguration.getUnitPrice()).thenReturn(unitPrice);
    when(customerRepository.findByPhoneNumber(any())).thenReturn(Optional.of(customer));
    when(orderRepository.save(any())).thenReturn(persistedOrder);
    when(orderNumberGenerator.generateOrderNumber()).thenReturn(orderNumber);
    when(timeService.now()).thenReturn(timestamp);


    var result = underTest.createOrder(createOrderRequestDTO);

    verify(customerRepository).findByPhoneNumber(createOrderRequestDTO.phoneNumber());
    verify(appConfiguration).getUnitPrice();
    verify(orderNumberGenerator).generateOrderNumber();
    verify(timeService).now();
    verify(customerRepository).findByPhoneNumber(createOrderRequestDTO.phoneNumber());

    var captor = ArgumentCaptor.forClass(Order.class);
    verify(orderRepository).save(captor.capture());

    var savedOrder = captor.getValue();

    assertThat(savedOrder.getCreatedAt()).isEqualTo(timestamp);
    assertThat(savedOrder.getUpdatedAt()).isEqualTo(timestamp);

    assertOrderRequestDtoToEntityMapping(customer, createOrderRequestDTO, savedOrder);
    assertOrderEntityToDtoMapping(persistedOrder, result);

  }

  @Test
  void createOrderShouldCreateCustomerThenOrderIfCustomerDoesNotExist() {

    var customer = TestHelper.createCustomer();
    var persistedOrder = TestHelper.createOrder();
    var createOrderRequestDTO = TestHelper.createCreateOrderRequestDTO();

    when(appConfiguration.getUnitPrice()).thenReturn(unitPrice);
    when(customerRepository.findByPhoneNumber(any())).thenReturn(Optional.empty());
    when(customerRepository.save(any())).thenReturn(customer);
    when(orderRepository.save(any())).thenReturn(persistedOrder);
    when(orderNumberGenerator.generateOrderNumber()).thenReturn(orderNumber);
    when(timeService.now()).thenReturn(timestamp);

    var result = underTest.createOrder(createOrderRequestDTO);

    verify(customerRepository).findByPhoneNumber(createOrderRequestDTO.phoneNumber());
    verify(appConfiguration).getUnitPrice();
    verify(orderNumberGenerator).generateOrderNumber();
    verify(timeService).now();
    verify(customerRepository).findByPhoneNumber(createOrderRequestDTO.phoneNumber());


    var customerCaptor = ArgumentCaptor.forClass(Customer.class);
    verify(customerRepository).save(customerCaptor.capture());

    var inCustomer = customerCaptor.getValue();

    assertThat(inCustomer.getFirstname()).isEqualTo(createOrderRequestDTO.firstname());
    assertThat(inCustomer.getLastname()).isEqualTo(createOrderRequestDTO.lastname());
    assertThat(inCustomer.getPhoneNumber()).isEqualTo(createOrderRequestDTO.phoneNumber());

    var orderCaptor = ArgumentCaptor.forClass(Order.class);
    verify(orderRepository).save(orderCaptor.capture());

    var savedOrder = orderCaptor.getValue();

    assertOrderRequestDtoToEntityMapping(customer, createOrderRequestDTO, savedOrder);
    assertOrderEntityToDtoMapping(persistedOrder, result);
  }


  @Test
  void updateOrderShouldUpdateOrderAndReturnIt() {

    var updateOrderRequestDTO = TestHelper.createUpdateOrderRequestDTO();
    var existingOrder = TestHelper.createOrder();
    var updatedOrder = TestHelper.createOrder();

    when(orderRepository.findByNumber(any())).thenReturn(Optional.of(existingOrder));
    when(orderRepository.save(any())).thenReturn(updatedOrder);
    when(timeService.now()).thenReturn(timestamp);
    when(appConfiguration.getUnitPrice()).thenReturn(unitPrice);

    var result = underTest.updateOrder(orderNumber, updateOrderRequestDTO);

    verify(orderRepository).findByNumber(orderNumber);
    verify(appConfiguration).getUnitPrice();
    verify(timeService).now();


    var captor = ArgumentCaptor.forClass(Order.class);
    verify(orderRepository).save(captor.capture());

    var savedOrder = captor.getValue();

    assertThat(savedOrder.getUpdatedAt()).isEqualTo(timestamp);
    assertOrderRequestDtoToEntityMapping(updateOrderRequestDTO, existingOrder, savedOrder);
    assertOrderEntityToDtoMapping(updatedOrder, result);

  }


  @Test
  void updateOrderShouldThrowExceptionIfOrderDoesNotExist() {


    var updateOrderRequestDTO = TestHelper.createUpdateOrderRequestDTO();

    when(orderRepository.findByNumber(any())).thenReturn(Optional.empty());

    assertThatExceptionOfType(OrderNotFoundException.class)
        .isThrownBy(() -> underTest.updateOrder(orderNumber, updateOrderRequestDTO));


  }

  private void assertOrderRequestDtoToEntityMapping(Customer customer, CreateOrderRequestDTO requestDTO, Order entity) {
    assertThat(entity.getNumber()).isEqualTo(orderNumber);
    assertThat(entity.getCustomer()).isEqualTo(customer);
    assertThat(entity.getQuantity()).isEqualTo(requestDTO.quantity());
    assertThat(entity.getDeliveryAddress()).isEqualTo(requestDTO.deliveryAddress());
    assertThat(entity.getTotalPrice()).isEqualTo(requestDTO.quantity() * unitPrice);
  }

  private static void assertOrderEntityToDtoMapping(Order entity, OrderResponseDTO dto) {
    assertThat(dto.createdAt()).isEqualTo(entity.getCreatedAt());
    assertThat(dto.updatedAt()).isEqualTo(entity.getUpdatedAt());
    assertThat(dto.orderNumber()).isEqualTo(entity.getNumber());
    assertThat(dto.quantity()).isEqualTo(entity.getQuantity());
    assertThat(dto.totalPrice()).isEqualTo(entity.getTotalPrice());
    assertThat(dto.deliveryAddress()).isEqualTo(entity.getDeliveryAddress());
    assertThat(dto.firstname()).isEqualTo(entity.getCustomer().getFirstname());
    assertThat(dto.lastname()).isEqualTo(entity.getCustomer().getLastname());
    assertThat(dto.phoneNumber()).isEqualTo(entity.getCustomer().getPhoneNumber());
  }

  private void assertOrderRequestDtoToEntityMapping(
      UpdateOrderRequestDTO updateOrderRequestDTO,
      Order existingOrder,
      Order savedOrder) {

    assertThat(existingOrder)
        .usingRecursiveComparison()
        .ignoringFields("totalPrice", "quantity", "deliveryAddress", "updatedAt" )
        .isEqualTo(savedOrder);

    assertThat(savedOrder.getTotalPrice()).isEqualTo(updateOrderRequestDTO.quantity() * unitPrice);
    assertThat(savedOrder.getQuantity()).isEqualTo(updateOrderRequestDTO.quantity());
  }

}

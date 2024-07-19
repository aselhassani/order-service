package com.orderservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order {

  private static final Double UNIT_PRICE = 1.33;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique = true, nullable = false)
  private String number;

  @With
  private Integer quantity;

  @With
  private String deliveryAddress;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updatedAt")
  @With
  private Instant updatedAt;

  @Column(name = "total_price")
  @With
  private Double totalPrice;

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

}

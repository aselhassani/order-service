package com.orderservice.repository;

import com.orderservice.model.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

  Optional<Order> findByNumber(String number);

}

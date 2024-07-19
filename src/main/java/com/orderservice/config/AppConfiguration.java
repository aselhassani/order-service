package com.orderservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

  @Value("${business_rules.unit_price}")
  private String unitPrice;

  public Double getUnitPrice() {
    return Double.parseDouble(unitPrice);
  }

}

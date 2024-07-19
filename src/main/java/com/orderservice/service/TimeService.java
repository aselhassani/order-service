package com.orderservice.service;

import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class TimeService {

  public Instant now() {
    return Instant.now();
  }

}

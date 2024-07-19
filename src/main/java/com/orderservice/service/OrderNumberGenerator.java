package com.orderservice.service;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

@Service
public class OrderNumberGenerator {

  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final int RANDOM_PART_LENGTH = 6;
  private static final SecureRandom random = new SecureRandom();


  public String generateOrderNumber() {

    return IntStream.range(0, RANDOM_PART_LENGTH)
        .mapToObj(i -> String.valueOf(CHARACTERS.charAt(random.nextInt(CHARACTERS.length()))))
        .collect(Collectors.joining());
  }

}

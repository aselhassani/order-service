package com.orderservice.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

  @Value("${spring.application.name}")
  private String applicationName;

  @Value("${info.maven_version}")
  private String version;

  @Bean
  public OpenAPI createOpenApi() {
    return new OpenAPI().info(new Info().title(applicationName).version(version));
  }
}

package com.techcorp.app.service;

import com.techcorp.app.domain.Company;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeeServiceConfig {

  @Bean
  public Company company() {
    return new Company();
  }

}

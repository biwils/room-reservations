package com.example.roomreservations.customer;

import com.example.roomreservations.model.repository.CustomerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CustomerConfiguration {

    @Bean
    CustomerService customerService(CustomerRepository customerRepository) {
        return new CustomerService(customerRepository);
    }

}

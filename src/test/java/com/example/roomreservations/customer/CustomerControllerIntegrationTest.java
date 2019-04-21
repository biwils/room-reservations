package com.example.roomreservations.customer;


import com.example.roomreservations.BaseIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;


public class CustomerControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CustomerController customerController;

    @Test
    public void should_register_customer() {
        // given
        String emailAddress = "john@example.com";
        String firstName = "John";
        String lastName = "Doe";
        RegisterCustomerCmd registerCustomerCmd = new RegisterCustomerCmd(emailAddress, firstName, lastName);

        // when
        ResponseEntity<CustomerDto> result = customerController.register(registerCustomerCmd);

        // then
        assertThat(result.getStatusCode()).isEqualTo(CREATED);
        assertThat(result.getBody().getId()).isNotNull();
        assertThat(result.getBody().getEmailAddress()).isEqualTo(emailAddress);
        assertThat(result.getBody().getFirstName()).isEqualTo(firstName);
        assertThat(result.getBody().getLastName()).isEqualTo(lastName);
    }

}

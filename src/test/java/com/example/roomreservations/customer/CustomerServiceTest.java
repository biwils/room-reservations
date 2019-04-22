package com.example.roomreservations.customer;

import com.example.roomreservations.model.Customer;
import com.example.roomreservations.model.CustomerFixture;
import com.example.roomreservations.model.repository.CustomerRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

import static com.example.roomreservations.customer.RegisterCustomerCmdFixture.johnDoe;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {

    private CustomerRepository customerRepository = mock(CustomerRepository.class);
    private CustomerService customerService = new CustomerService(customerRepository);

    @Rule
    public ExpectedException exceptionRule = none();

    @Test
    public void should_add_new_customer() {
        // given
        RegisterCustomerCmd newCustomer = johnDoe();
        when(customerRepository.findByEmailAddress(newCustomer.getEmailAddress())).thenReturn(empty());
        Customer customer = CustomerFixture.johnDoe();
        when(customerRepository.save(any())).thenReturn(customer);

        // when
        CustomerDto result = customerService.add(newCustomer);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getEmailAddress()).isEqualTo(customer.getEmailAddress());
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
    }

    @Test
    public void should_not_add_new_customer() {
        // given
        RegisterCustomerCmd newCustomer = johnDoe();
        when(customerRepository.findByEmailAddress(newCustomer.getEmailAddress())).thenReturn(Optional.of(CustomerFixture.johnDoe()));
        // If I had a choice in testing framework I'd use Spock which offers more elegant ways of testing exceptions.
        // I could have used Junit 5 but I chose to use latest stable Spring Boot Starter which comes with Junit 4
        // and I didn't want to mess up with excludes in pom just for the sake of a few test cases.
        exceptionRule.expect(EmailAddressAlreadyRegisteredException.class);
        exceptionRule.expectMessage("Customer with email address john@example.com is already registered");

        // when
        customerService.add(newCustomer);

        // then exception thrown
    }
}
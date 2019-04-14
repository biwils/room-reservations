package com.example.roomreservations.customer;

import com.example.roomreservations.model.Customer;
import com.example.roomreservations.model.repository.CustomerRepository;
import org.springframework.transaction.annotation.Transactional;

class CustomerService {

    private CustomerRepository customerRepository;

    CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerDto add(RegisterCustomerCmd registerCustomerCmd) {
        String emailAddress = registerCustomerCmd.getEmailAddress();
        customerRepository.findByEmailAddress(emailAddress).ifPresent(email -> {
            throw new EmailAddressAlreadyRegisteredException(emailAddress);
        });
        Customer customer = new Customer(emailAddress, registerCustomerCmd.getFirstName(), registerCustomerCmd.getLastName());
        Customer saved = customerRepository.save(customer);
        return new CustomerDto(saved.getId(), saved.getEmailAddress(), saved.getFirstName(), saved.getLastName());
    }

}

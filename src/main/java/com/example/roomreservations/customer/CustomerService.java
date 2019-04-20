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
        //some email validation could be useful but is out of scope
        String emailAddress = registerCustomerCmd.getEmailAddress();
        customerRepository.findByEmailAddress(emailAddress).ifPresent(email -> {
            throw new EmailAddressAlreadyRegisteredException(emailAddress);
        });
        Customer customer = customerRepository.save(new Customer(emailAddress, registerCustomerCmd.getFirstName(), registerCustomerCmd.getLastName()));
        return new CustomerDto(customer.getId(), customer.getEmailAddress(), customer.getFirstName(), customer.getLastName());
    }

}
